package com.recipe.book.web.domain.recipe.repository;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.dto.RecipeData;
import com.recipe.book.web.domain.recipe.dto.RecipePreview;
import com.recipe.book.web.domain.recipe.dto.RecipeView;
import com.recipe.book.web.global.config.security.access.AccessOwnerAndAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @EntityGraph(attributePaths = {"writer"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<RecipePreview> findAllPreviewBy(Pageable pageable);

    @Override
    boolean existsById(Long id);

    @EntityGraph(attributePaths = {"writer", "commentList"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<RecipeView> findViewById(Long id);

    @AccessOwnerAndAdmin
    Optional<RecipeData> findDataById(Long id);

    @EntityGraph(attributePaths = {"writer"}, type = EntityGraph.EntityGraphType.LOAD)
    List<RecipePreview> findFirst5ByCreateDateAfter(LocalDateTime createDate, Sort sort);

    @Modifying
    @Query("UPDATE Recipe r SET r.views = r.views + 1 WHERE r.id = :id")
    void updateViewsById(@Param("id") Long id);
}
