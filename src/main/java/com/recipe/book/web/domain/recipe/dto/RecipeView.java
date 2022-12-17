package com.recipe.book.web.domain.recipe.dto;

import com.recipe.book.web.domain.comment.dto.CommentView;
import com.recipe.book.web.domain.user.dto.UserInfo;
import com.recipe.book.web.global.config.security.Ownable;

import java.time.LocalDateTime;
import java.util.List;

public interface RecipeView extends Ownable {
    Long getId();

    String getTitle();

    List<String> getThumbnails();

    String getContent();

    Long getViews();

    List<CommentView> getCommentList();

    Long getCommentCount();

    @Override
    String getPrincipalName();

    UserInfo getWriter();

    LocalDateTime getCreateDate();

    Long getRecommendationCount();

    Long getAfter();

    Long getBefore();
}
