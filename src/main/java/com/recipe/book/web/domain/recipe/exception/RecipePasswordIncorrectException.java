package com.recipe.book.web.domain.recipe.exception;

import com.recipe.book.web.domain.common.exception.FieldException;

public class RecipePasswordIncorrectException extends FieldException {
    public RecipePasswordIncorrectException() {
        super("password", "게시물 대조 실패(이유:틀린 비밀번호)", "비밀번호가 틀립니다.");
    }
}
