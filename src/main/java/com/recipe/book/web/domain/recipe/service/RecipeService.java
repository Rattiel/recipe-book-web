package com.recipe.book.web.domain.recipe.service;

import com.recipe.book.web.domain.recipe.dto.RecipeData;
import com.recipe.book.web.domain.recipe.dto.RecipePreview;
import com.recipe.book.web.domain.recipe.dto.RecipeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {
    long create(String title, List<String> thumbnails,  String content);

    long update(long id, String title, List<String> thumbnails, String content);

    void delete(long id);

    Page<RecipePreview> findPageByPageable(Pageable pageable);

    RecipeData findDataById(long id);

    RecipeView findById(long id);
}
