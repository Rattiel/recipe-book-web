package com.recipe.book.web.global.config.security;

import com.recipe.book.web.domain.user.User;

public interface Ownable {
    User getOwner();
}
