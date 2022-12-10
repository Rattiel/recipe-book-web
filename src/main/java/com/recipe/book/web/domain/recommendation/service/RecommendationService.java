package com.recipe.book.web.domain.recommendation.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface RecommendationService {
    void create(long recipeId, UserDetails userDetails);

    void delete(long recipeId, UserDetails userDetails);

    boolean isRecommended(long recipeId, UserDetails userDetails);
}
