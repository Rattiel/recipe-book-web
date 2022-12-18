package com.recipe.book.web.domain.comment.controller;

import com.recipe.book.web.domain.comment.dto.CommentForm;
import com.recipe.book.web.domain.comment.exception.CommentNotFoundException;
import com.recipe.book.web.domain.comment.service.CommentService;
import com.recipe.book.web.domain.recipe.exception.RecipeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/recipe/{recipeId}/comment")
@Controller
public class CommentController {
    private final CommentService commentService;

    @ExceptionHandler({RecipeNotFoundException.class})
    public String redirectIndex() {
        return "redirect:/";
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            CommentNotFoundException.class
    })
    public String redirectRecipeView(
            HttpServletRequest request,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) instanceof Map<?, ?> pathVariables) {
            try {
                Long recipeId = Long.parseLong(String.valueOf(pathVariables.get("recipeId")));
                return redirectRecipeView(recipeId, pageable);
            } catch (Exception ignored) { }
        }

        return redirectIndex();
    }

    private String redirectRecipeView(Long recipeId, Pageable pageable) {
        return redirectRecipeView(recipeId, null, pageable);
    }

    private String redirectRecipeView(Long recipeId, Long commentId, Pageable pageable) {
        if (commentId == null) {
            return String.format(
                    "redirect:/recipe/%d?page=%d&size=%d",
                    recipeId,
                    pageable.getPageNumber(),
                    pageable.getPageSize()
            );
        } else {
            return String.format(
                    "redirect:/recipe/%d?page=%d&size=%d#comment-%d",
                    recipeId,
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    commentId
            );
        }
    }

    @PostMapping("/new/create")
    public String requestCreate(
            @PathVariable Long recipeId,
            @ModelAttribute CommentForm form,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        long commentId = commentService.create(recipeId, form.getContent());

        return redirectRecipeView(recipeId, commentId, pageable);
    }

    @PostMapping("/{id}/update")
    public String requestUpdate(
            @PathVariable Long recipeId,
            @PathVariable Long id,
            @ModelAttribute CommentForm form,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        long commentId = commentService.update(id, form.getContent());

        return redirectRecipeView(recipeId, commentId, pageable);
    }

    @PostMapping("/{commentId}/delete")
    public String requestDelete(
            @PathVariable Long recipeId,
            @PathVariable Long commentId,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        commentService.delete(commentId);

        return redirectRecipeView(recipeId, pageable);
    }
}
