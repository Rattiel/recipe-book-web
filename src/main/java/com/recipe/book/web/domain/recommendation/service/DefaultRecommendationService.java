package com.recipe.book.web.domain.recommendation.service;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.exception.RecipeNotFoundException;
import com.recipe.book.web.domain.recipe.repository.RecipeRepository;
import com.recipe.book.web.domain.recommendation.repository.RecommendationRepository;
import com.recipe.book.web.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class DefaultRecommendationService implements RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    @Override
    public void create(long postId, UserDetails userDetails) {
        Recipe recipe = findRecipe(postId);
    }

    @Override
    public void remote(long postId, UserDetails userDetails) {
        Recipe recipe = findRecipe(postId);
    }

    private Recipe findRecipe(long postId) {
        return recipeRepository.findById(postId)
                .orElseThrow(RecipeNotFoundException::new);
    }
}
