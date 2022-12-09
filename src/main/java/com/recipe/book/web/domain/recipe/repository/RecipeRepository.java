package com.recipe.book.web.domain.recipe.repository;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.dto.RecipeData;
import com.recipe.book.web.domain.recipe.dto.RecipePreview;
import com.recipe.book.web.domain.recipe.dto.RecipeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @EntityGraph(attributePaths = {"owner"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<RecipePreview> findAllPreviewBy(Pageable pageable);

    @EntityGraph(attributePaths = {"owner"}, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    Optional<Recipe> findById(Long id);

    @EntityGraph(attributePaths = {"owner"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<RecipeView> findViewById(Long id);

    Optional<RecipeData> findDataById(Long id);

    @Modifying
    @Query("UPDATE Recipe r SET r.views = r.views + 1 WHERE r.id = :id")
    void updateViewsById(@Param("id") Long id);
}
