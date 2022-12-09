package com.recipe.book.web.domain.recipe.service;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.dto.RecipeData;
import com.recipe.book.web.domain.recipe.dto.RecipePreview;
import com.recipe.book.web.domain.recipe.dto.RecipeView;
import com.recipe.book.web.domain.recipe.exception.RecipeNotFoundException;
import com.recipe.book.web.domain.recipe.exception.RecipePasswordIncorrectException;
import com.recipe.book.web.domain.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class DefaultRecipeService implements RecipeService {
    private final RecipeRepository recipeRepository;

    @Override
    public long create(String title, List<String> thumbnails, String content) {
        Recipe recipe = Recipe.create(title, thumbnails, content);

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
    public Page<RecipePreview> findPageByPageable(Pageable pageable) {
        return recipeRepository.findAllPreviewBy(pageable);
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
}
