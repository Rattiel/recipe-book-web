package com.recipe.book.web.domain.comment.service;

public interface CommentService {
    long create(long recipeId, String content);

    long update(long id, String content);

    void delete(long id);
}
