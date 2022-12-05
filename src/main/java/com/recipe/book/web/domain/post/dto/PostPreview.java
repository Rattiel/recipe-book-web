package com.recipe.book.web.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

public interface PostPreview {
    Long getId();
    String getTitle();
    List<String> getThumbnails();
    String getViewsCount();
    LocalDateTime getCreateDate();
}
