package com.recipe.book.web.domain.recipe.service;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.dto.RecipeData;
import com.recipe.book.web.domain.recipe.dto.RecipePreview;
import com.recipe.book.web.domain.recipe.dto.RecipeView;
import com.recipe.book.web.domain.recipe.exception.RecipeNotFoundException;
import com.recipe.book.web.domain.recipe.exception.RecipePasswordIncorrectException;
import com.recipe.book.web.domain.recipe.repository.RecipeRepository;
import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class DefaultRecipeService implements RecipeService {
    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    @Override
    public long create(String title, List<String> thumbnails, String content) {
        User writer = getUser();

        Recipe recipe = Recipe.create(title, writer, thumbnails, content);

        recipeRepository.save(recipe);

        return recipe.getId();
    }

    @Override
    public long update(
            long id,
            String title,
            List<String> thumbnails,
            String content
    ) throws RecipePasswordIncorrectException {
        Recipe recipe = find(id);

        recipe.update(title, thumbnails, content);

        return recipe.getId();
    }

    @Override
    public void delete(long id)
            throws RecipePasswordIncorrectException {
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

        return recipeRepository.findFirst5ByCreateDateAfter(
                lastWeek,
                Sort.by(List.of(
                        new Sort.Order(Sort.Direction.DESC, "recommendationCount"),
                        new Sort.Order(Sort.Direction.DESC, "id")
                ))
        );
    }

    @Transactional(readOnly = true)
    @Override
    public RecipeData findDataById(long id) {
        return recipeRepository.findDataById(id)
                .orElseThrow(RecipeNotFoundException::new);
    }

    @Override
    public RecipeView findById(long id) {
        recipeRepository.updateViewsById(id);

        return recipeRepository.findViewById(id)
                .orElseThrow(RecipeNotFoundException::new);
    }

    private Recipe find(long id) {
        return recipeRepository.findById(id)
                .orElseThrow(RecipeNotFoundException::new);
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("사용자 정보 불러오기 실패(이유: 인증 안됌)");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> {throw new AccessDeniedException("사용자 정보 불러오기 실패(이유: 인증 안됌)");});
        }

        throw new ClassCastException("사용자 아이디 가져오기 실패(이유: UserDetails 로 형변환 실패)");
    }
}
