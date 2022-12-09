package com.recipe.book.web.domain.recipe.dto;

import java.util.List;

public interface RecipeData {
    String getTitle();

    List<String> getThumbnails();

    String getContent();
}
