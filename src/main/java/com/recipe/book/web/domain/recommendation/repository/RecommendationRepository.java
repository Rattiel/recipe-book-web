package com.recipe.book.web.domain.recommendation.repository;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recommendation.Recommendation;
import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.global.config.security.access.AccessOnlyOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    @AccessOnlyOwner
    @Override
    Optional<Recommendation> findById(Long id);

    @AccessOnlyOwner
    Optional<Recommendation> findByOwnerAndRecipe(User owner, Recipe recipe);

    boolean existsByOwnerAndRecipe(User owner, Recipe recipe);

    @SuppressWarnings("unchecked")
    @AccessOnlyOwner
    @Override
    Recommendation save(Recommendation entity);
}
