package com.recipe.book.web.domain.recommendation.repository;

import com.recipe.book.web.domain.recommendation.Recommendation;
import com.recipe.book.web.domain.recommendation.RecommendationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, RecommendationId> {
}
