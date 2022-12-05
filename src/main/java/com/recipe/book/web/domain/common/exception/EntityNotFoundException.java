package com.recipe.book.web.domain.common.exception;

public abstract class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String msg, String reason) {
        super(msg, reason);
    }
}
