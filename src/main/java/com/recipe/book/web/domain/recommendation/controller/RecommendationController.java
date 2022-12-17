package com.recipe.book.web.domain.recommendation.controller;

import com.recipe.book.web.domain.recipe.exception.RecipeNotFoundException;
import com.recipe.book.web.domain.recommendation.service.RecommendationService;
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
@RequestMapping("/recipe/{recipeId}/recommendation")
@Controller
public class RecommendationController {
    private final RecommendationService recommendationService;

    @ExceptionHandler(
            {
                    RecipeNotFoundException.class,
        }
    )
    public String redirectIndex() {
        return "redirect:/";
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    public String redirectRecipe(
            HttpServletRequest request,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) instanceof Map<?, ?> pathVariables) {
            try {
                Long recipeId = Long.parseLong(String.valueOf(pathVariables.get("recipeId")));
                return String.format(
                        "redirect:/recipe/%d?page=%d&size=%d",
                        recipeId,
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                );
            } catch (Exception ignored) { }
        }

        return redirectIndex();
    }

    private String redirectRecipe(long recipeId, Pageable pageable) {
        return String.format(
                "redirect:/recipe/%d?page=%d&size=%d",
                recipeId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @GetMapping("/create")
    public String requestCreate(
            @PathVariable Long recipeId,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        recommendationService.create(recipeId);

        return redirectRecipe(recipeId, pageable);
    }

    @GetMapping("/delete")
    public String requestDelete(
            @PathVariable Long recipeId,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        recommendationService.delete(recipeId);

        return redirectRecipe(recipeId, pageable);
    }
}
