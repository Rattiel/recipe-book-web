package com.recipe.book.web.domain.recommendation.service;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.exception.RecipeNotFoundException;
import com.recipe.book.web.domain.recipe.repository.RecipeRepository;
import com.recipe.book.web.domain.recommendation.Recommendation;
import com.recipe.book.web.domain.recommendation.repository.RecommendationRepository;
import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.domain.user.repository.UserRepository;
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

    private final UserRepository userRepository;

    @Override
    public void create(long recipeId, UserDetails userDetails) {
        User user = findUser(userDetails.getUsername());

        if (!recipeRepository.existsById(recipeId)) {
            throw new RecipeNotFoundException();
        }

        Recipe recipe = Recipe.withId(recipeId);

        if (!recommendationRepository.existsByOwnerAndRecipe(user, recipe)) {
            Recommendation recommendation = Recommendation.create(user, recipe);

            recommendationRepository.save(recommendation);
        }
    }

    @Override
    public void delete(long recipeId, UserDetails userDetails) {
        User user = findUser(userDetails.getUsername());

        if (!recipeRepository.existsById(recipeId)) {
            throw new RecipeNotFoundException();
        }

        Recipe recipe = Recipe.withId(recipeId);

        Optional<Recommendation> recommendationOptional = recommendationRepository.findByOwnerAndRecipe(user, recipe);

        recommendationOptional.ifPresent(recommendationRepository::delete);
    }

    @Override
    public boolean isRecommended(long recipeId, UserDetails userDetails) {
        if (userDetails == null) {
            return false;
        }

        User user = findUser(userDetails.getUsername());

        if (!recipeRepository.existsById(recipeId)) {
            throw new RecipeNotFoundException();
        }

        Recipe recipe = Recipe.withId(recipeId);

        return recommendationRepository.existsByOwnerAndRecipe(user, recipe);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("사용자 정보 불러오기 실패(이유: 인증 안됌)"));
    }
}
