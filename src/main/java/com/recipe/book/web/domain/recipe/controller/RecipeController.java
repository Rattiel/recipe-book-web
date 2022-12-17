package com.recipe.book.web.domain.recipe.controller;

import com.recipe.book.web.domain.recipe.dto.*;
import com.recipe.book.web.domain.recipe.exception.RecipeNotFoundException;
import com.recipe.book.web.domain.recipe.service.RecipeService;
import com.recipe.book.web.domain.recommendation.service.RecommendationService;
import com.recipe.book.web.global.config.security.Ownable;
import com.recipe.book.web.global.config.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/recipe")
@Controller
public class RecipeController {
    private final static int MAX_NAV_ITEM = 5;

    private final RecipeService recipeService;

    private final RecommendationService recommendationService;

    private final SecurityUtil securityUtil;

    @ExceptionHandler({RecipeNotFoundException.class})
    public String redirectIndex() {
        return "redirect:/";
    }

    @ModelAttribute
    public void bindYesterday(Model model) {
        LocalDateTime yesterday = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        model.addAttribute("yesterday", yesterday);
    }

    @ModelAttribute
    public void bindPageable(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
    }

    @GetMapping("/new/create")
    public String renderCreatePage(Model model) {
        bindCreateForm(model);

        return "recipe/create";
    }

    @PostMapping("/new/create")
    public String requestCreate(
            @Validated @ModelAttribute("form") RecipeForm form,
            BindingResult bindingResult,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (bindingResult.hasErrors()) {
            return "recipe/create";
        }

        long postId = recipeService.create(
                form.getTitle(),
                form.getThumbnails(),
                form.getContent()
        );

        return String.format(
                "redirect:/recipe/%d?page=%d&size=%d",
                postId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @GetMapping("/{id}/update")
    public String renderUpdatePage(
            @PathVariable Long id,
            Model model
    ) {
        RecipeData data = recipeService.findDataById(id);
        bindUpdateForm(data, model);

        return "recipe/update";
    }

    @PostMapping("/{id}/update")
    public String requestUpdate(
            @PathVariable Long id,
            @Validated @ModelAttribute("form") RecipeForm form,
            BindingResult bindingResult,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (bindingResult.hasErrors()) {
            return "recipe/update";
        }

        long postId = recipeService.update(
                id,
                form.getTitle(),
                form.getThumbnails(),
                form.getContent()
        );

        return String.format(
                "redirect:/recipe/%d?page=%d&size=%d",
                postId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @PostMapping("/{id}/delete")
    public String requestDelete(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        recipeService.delete(id);

        return String.format(
                "redirect:/recipe?page=%d&size=%d",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @GetMapping
    public String renderList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<RecipePreview> recipePreviewPage = recipeService.findPreviewPageByPageable(pageable);
        bindPreviewPage(recipePreviewPage, model);
        bindPageNavBar(recipePreviewPage, model);

        List<RecipePreview> top5RecipePreviewList = recipeService.findPreviewListByTop5InWeek();
        bindTop5PreviewPage(top5RecipePreviewList, model);

        return "recipe/list";
    }

    @GetMapping("/{id}")
    public String renderView(
            @PathVariable Long id,
            Model model
    ) {
        RecipeView recipe = recipeService.findById(id);
        bindView(recipe, model);
        bindEditable(recipe, model);
        bindRecommended(id, model);

        return "recipe/view";
    }

    private void bindEditable(Ownable recipe, Model model) {
        boolean editable = securityUtil.checkEditable(recipe);
        model.addAttribute("editable", editable);
    }

    private void bindRecommended(long recipeId, Model model) {
        boolean recommended = recommendationService.isRecommended(recipeId);
        model.addAttribute("recommended", recommended);
    }

    private void bindCreateForm(Model model) {
        RecipeForm form = new RecipeForm();
        model.addAttribute("form", form);
    }

    private void bindUpdateForm(RecipeData data, Model model) {
        RecipeForm form = RecipeForm.from(data);
        model.addAttribute("form", form);
    }

    private void bindPreviewPage(Page<RecipePreview> previewList, Model model) {
        model.addAttribute("previewList", previewList);
    }

    private void bindTop5PreviewPage(List<RecipePreview> top5PreviewList, Model model) {
        model.addAttribute("top5PreviewList", top5PreviewList);
    }

    private void bindPageNavBar(Page<?> page, Model model) {
        int navStartPage = page.getNumber() - page.getNumber() % MAX_NAV_ITEM;
        model.addAttribute("navStartPage", navStartPage);

        int navLastPage = Math.min(navStartPage + MAX_NAV_ITEM - 1, page.getTotalPages() - 1);
        navLastPage = Math.max(navLastPage, 0);
        model.addAttribute("navLastPage", navLastPage);

        boolean showBeforeNav = navStartPage >= MAX_NAV_ITEM;
        model.addAttribute("showBeforeNav", showBeforeNav);

        boolean showNextNav = navLastPage < page.getTotalPages() - 1;
        model.addAttribute("showNextNav", showNextNav);

        model.addAttribute("pageNumber", page.getNumber());
    }

    private void bindView(RecipeView view, Model model) {
        model.addAttribute("view", view);
    }
}
