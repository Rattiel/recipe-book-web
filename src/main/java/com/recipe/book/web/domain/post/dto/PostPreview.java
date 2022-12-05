package com.recipe.book.web.domain.post.dto;

import com.recipe.book.web.domain.user.dto.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

public interface PostPreview {
    Long getId();
    String getTitle();
    List<String> getThumbnails();
    UserInfo getOwner();
    String getViews();
    LocalDateTime getCreateDate();
}
