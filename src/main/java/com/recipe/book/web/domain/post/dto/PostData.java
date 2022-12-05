package com.recipe.book.web.domain.post.dto;

import java.util.List;

public interface PostData {
    String getTitle();

    List<String> getThumbnails();

    String getContent();
}
