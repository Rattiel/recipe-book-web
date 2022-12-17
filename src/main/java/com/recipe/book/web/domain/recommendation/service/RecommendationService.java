package com.recipe.book.web.domain.recommendation.service;

public interface RecommendationService {
    void create(long recipeId);

    void delete(long recipeId);

    boolean isRecommended(long recipeId);
}
