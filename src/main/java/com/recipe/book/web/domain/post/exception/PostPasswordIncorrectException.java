package com.recipe.book.web.domain.post.exception;

import com.recipe.book.web.domain.common.exception.FieldException;

public class PostPasswordIncorrectException extends FieldException {
    public PostPasswordIncorrectException() {
        super("password", "게시물 대조 실패(이유:틀린 비밀번호)", "비밀번호가 틀립니다.");
    }
}
