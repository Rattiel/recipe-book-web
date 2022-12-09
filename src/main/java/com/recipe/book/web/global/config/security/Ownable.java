package com.recipe.book.web.global.config.security;

import com.recipe.book.web.domain.user.dto.UserInfo;

public interface Ownable<T extends UserInfo> {
    T getOwner();
}
