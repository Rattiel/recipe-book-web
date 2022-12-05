package com.recipe.book.web.domain.post.exception;

import com.recipe.book.web.domain.common.exception.EntityNotFoundException;

public class PostNotFoundException extends EntityNotFoundException {
    public PostNotFoundException() {
        super("게시물 조회 실패(이유:존재하지 않는 번호)", "게시물이 존재하지 않습니다.");
    }
}
