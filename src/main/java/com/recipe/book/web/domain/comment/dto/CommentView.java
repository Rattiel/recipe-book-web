package com.recipe.book.web.domain.comment.dto;

import com.recipe.book.web.domain.user.dto.UserInfo;
import com.recipe.book.web.global.config.security.Ownable;

import java.time.LocalDateTime;

public interface CommentView extends Ownable {
    Long getId();

    UserInfo getWriter();
    @Override
    String getPrincipalName();

    String getContent();

    LocalDateTime getCreateDate();
}
