package com.recipe.book.web.domain.recipe.dto;

import com.recipe.book.web.global.config.security.Ownable;

import java.util.List;

public interface RecipeData extends Ownable {
    String getTitle();

    List<String> getThumbnails();

    @Override
    String getPrincipalName();

    String getContent();
}
