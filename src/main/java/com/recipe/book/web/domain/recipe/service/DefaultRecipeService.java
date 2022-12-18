package com.recipe.book.web.domain.recipe.service;

import com.recipe.book.web.domain.common.exception.FieldException;
import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.dto.RecipeData;
import com.recipe.book.web.domain.recipe.dto.RecipePreview;
import com.recipe.book.web.domain.recipe.dto.RecipeView;
import com.recipe.book.web.domain.recipe.exception.RecipeNotFoundException;
import com.recipe.book.web.domain.recipe.repository.RecipeRepository;
import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.global.config.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class DefaultRecipeService implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final SecurityUtil securityUtil;

    @Override
    public long create(String title, List<String> thumbnails, String content) throws FieldException {
        check(title, thumbnails, content);
        User writer = securityUtil.getUser();
        Recipe recipe = Recipe.create(title, writer, thumbnails, content);
        recipeRepository.save(recipe);
        return recipe.getId();
    }

    @Override
    public long update(long id, String title, List<String> thumbnails, String content) throws FieldException {
        check(title, thumbnails, content);
        Recipe recipe = find(id);
        recipe.update(title, thumbnails, content);
        return recipe.getId();
    }

    @Override
    public void delete(long id) {
        Recipe recipe = find(id);
        recipeRepository.delete(recipe);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RecipePreview> findPreviewPageByPageable(Pageable pageable) {
        return recipeRepository.findAllPreviewBy(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RecipePreview> findPreviewListByTop5InWeek() {
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);
        return recipeRepository.findFirst5ByCreateDateAfter(lastWeek,
                Sort.by(List.of(new Sort.Order(Sort.Direction.DESC, "recommendationCount"),
                                new Sort.Order(Sort.Direction.DESC, "id")
                        )
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public RecipeData findDataById(long id) {
        return recipeRepository.findDataById(id).orElseThrow(RecipeNotFoundException::new);
    }

    @Override
    public RecipeView findById(long id) {
        recipeRepository.updateViewsById(id);
        return recipeRepository.findViewById(id).orElseThrow(RecipeNotFoundException::new);
    }

    private Recipe find(long id) {
        return recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
    }

    private void check(String title, List<String> thumbnails, String content) {
        checkTitle(title);
        checkThumbnails(thumbnails);
        checkContent(content);
    }

    private void checkContent(String content) {
        if (content.isBlank()) {
            throw new FieldException("본문 체크 실패(이유: 공백)", "content", "본문을 입력해주세요.");
        }
    }

    private void checkTitle(String title) {
        if (title.isBlank()) {
            throw new FieldException("제목 체크 실패(이유: 공백)", "title", "제목을 입력해주세요.");
        }
    }

    private void checkThumbnails(List<String> thumbnails) {
        if (thumbnails.isEmpty()) {
            throw new FieldException("썸네일 체크 실패(이유: 이미지 없음)", "thumbnails", "썸네일을 업로드 해주세요.");
        }
    }
}
