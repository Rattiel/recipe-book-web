package com.recipe.book.web.domain.recipe.dto;

import com.recipe.book.web.domain.user.dto.UserInfo;
import com.recipe.book.web.global.config.security.Ownable;

import java.time.LocalDateTime;
import java.util.List;

public interface RecipePreview extends Ownable {
    Long getId();

    String getTitle();

    List<String> getThumbnails();

    @Override
    String getPrincipalName();

    UserInfo getWriter();

    LocalDateTime getCreateDate();

    Long getCommentCount();

    Long getRecommendationCount();
}
