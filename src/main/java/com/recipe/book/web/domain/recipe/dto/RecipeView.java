package com.recipe.book.web.domain.recipe.dto;

import com.recipe.book.web.domain.user.dto.UserInfo;
import com.recipe.book.web.global.config.security.Ownable;

import java.time.LocalDateTime;
import java.util.List;

public interface RecipeView extends Ownable<UserInfo> {
    Long getId();

    String getTitle();

    List<String> getThumbnails();

    String getContent();

    Long getViews();

    @Override
    UserInfo getOwner();

    LocalDateTime getCreateDate();

    LocalDateTime getModifiedDate();

    Boolean getRecommended();

    Long getRecommendationCount();

    Long getAfter();

    Long getBefore();
}
