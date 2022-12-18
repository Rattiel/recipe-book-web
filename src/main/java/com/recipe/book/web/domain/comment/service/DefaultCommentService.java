package com.recipe.book.web.domain.comment.service;

import com.recipe.book.web.domain.comment.Comment;
import com.recipe.book.web.domain.comment.exception.CommentNotFoundException;
import com.recipe.book.web.domain.comment.repository.CommentRepository;
import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.exception.RecipeNotFoundException;
import com.recipe.book.web.domain.recipe.repository.RecipeRepository;
import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.global.config.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class DefaultCommentService implements CommentService {
    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final SecurityUtil securityUtil;

    @Override
    public long create(long recipeId, String content) {
        User writer = securityUtil.getUser();

        Recipe recipe = findRecipe(recipeId);

        Comment comment = Comment.create(recipe, writer, content);

        commentRepository.save(comment);

        return comment.getId();
    }

    @Override
    public long update(long id, String content) {
        Comment comment = findComment(id);

        comment.update(content);

        return comment.getId();
    }

    @Override
    public void delete(long id) {
        Comment comment = findComment(id);
        commentRepository.delete(comment);
    }

    private Comment findComment(long id) {
        return commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);
    }

    private Recipe findRecipe(long id) {
        return recipeRepository.findById(id)
                .orElseThrow(RecipeNotFoundException::new);
    }
}
