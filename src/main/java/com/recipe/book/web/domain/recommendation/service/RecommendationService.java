package com.recipe.book.web.domain.recommendation.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface RecommendationService {
    void create(long postId, UserDetails userDetails);

    void remote(long postId, UserDetails userDetails);
}
