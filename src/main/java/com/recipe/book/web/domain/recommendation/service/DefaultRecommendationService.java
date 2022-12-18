package com.recipe.book.web.domain.recommendation.service;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.exception.RecipeNotFoundException;
import com.recipe.book.web.domain.recipe.repository.RecipeRepository;
import com.recipe.book.web.domain.recommendation.Recommendation;
import com.recipe.book.web.domain.recommendation.repository.RecommendationRepository;
import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.domain.user.repository.UserRepository;
import com.recipe.book.web.global.config.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class DefaultRecommendationService implements RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecipeRepository recipeRepository;
    private final SecurityUtil securityUtil;

    @Override
    public void create(long recipeId) {
        User user = securityUtil.getUser();
        Recipe recipe = findRecipe(recipeId);
        if (!recommendationRepository.existsByOwnerAndRecipe(user, recipe)) {
            Recommendation recommendation = Recommendation.create(user, recipe);
            recommendationRepository.save(recommendation);
        }
    }

    @Override
    public void delete(long recipeId) {
        User user = securityUtil.getUser();
        Recipe recipe = findRecipe(recipeId);
        Optional<Recommendation> recommendationOptional = recommendationRepository.findByOwnerAndRecipe(user, recipe);
        recommendationOptional.ifPresent(recommendationRepository::delete);
    }

    @Override
    public boolean isRecommended(long recipeId) {
        try {
            User user = securityUtil.getUser();
            Recipe recipe = findRecipe(recipeId);
            return recommendationRepository.existsByOwnerAndRecipe(user, recipe);
        } catch (AccessDeniedException e) {
            return false;
        }
    }

    private Recipe findRecipe(long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
    }
}
