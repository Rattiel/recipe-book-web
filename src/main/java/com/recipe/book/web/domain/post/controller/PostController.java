package com.recipe.book.web.domain.post.controller;

import com.recipe.book.web.domain.common.exception.Fieldable;
import com.recipe.book.web.domain.post.Post;
import com.recipe.book.web.domain.post.dto.*;
import com.recipe.book.web.domain.post.exception.PostNotFoundException;
import com.recipe.book.web.domain.post.exception.PostPasswordIncorrectException;
import com.recipe.book.web.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@RequestMapping("/post")
@Controller
public class PostController {
    private final static int MAX_NAV_ITEM = 5;

    private final PostService postService;

    @ExceptionHandler({PostNotFoundException.class})
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

        return "post/create";
    }

    @PostMapping("/new/create")
    public String requestCreate(
            @Validated @ModelAttribute("form") PostForm form,
            BindingResult bindingResult,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (bindingResult.hasErrors()) {
            return "post/create";
        }

        Post post = postService.create(
                form.getTitle(),
                form.getThumbnails(),
                form.getContent()
        );

        return String.format(
                "redirect:/post/%d?page=%d&size=%d",
                post.getId(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @GetMapping("/{id}/update")
    public String renderUpdatePage(
            @PathVariable Long id,
            Model model
    ) {
        PostData data = postService.findDataById(id);
        bindUpdateForm(data, model);

        return "post/update";
    }

    @PostMapping("/{id}/update")
    public String requestUpdate(
            @PathVariable Long id,
            @Validated @ModelAttribute("form") PostForm form,
            BindingResult bindingResult,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (bindingResult.hasErrors()) {
            return "post/update";
        }

        try {
            Post post = postService.update(
                    id,
                    form.getTitle(),
                    form.getThumbnails(),
                    form.getContent()
            );

            return String.format(
                    "redirect:/post/%d?page=%d&size=%d",
                    post.getId(),
                    pageable.getPageNumber(),
                    pageable.getPageSize()
            );
        } catch (PostPasswordIncorrectException e) {
            bindFieldError(bindingResult, e);
            return "post/update";
        }
    }

    @PostMapping("/{id}/delete")
    public String requestDelete(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        postService.delete(id);

        return String.format(
                "redirect:/post?page=%d&size=%d",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @GetMapping
    public String renderList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<PostPreview> postPreviewPage = postService.list(pageable);
        bindPostPreviewPage(postPreviewPage, model);
        bindPageNavBar(postPreviewPage, model);

        return "post/list";
    }

    @GetMapping("/{id}")
    public String renderView(
            @PathVariable Long id,
            Model model
    ) {
        Post post = postService.findById(id);
        bindPostView(post, model);

        return "post/view";
    }

    private void bindCreateForm(Model model) {
        PostForm form = new PostForm();
        model.addAttribute("form", form);
    }

    private void bindUpdateForm(PostData data, Model model) {
        PostForm form = PostForm.from(data);
        model.addAttribute("form", form);
    }

    private void bindPostPreviewPage(Page<PostPreview> postPreviewPage, Model model) {
        model.addAttribute("postPreviewPage", postPreviewPage);
    }

    private void bindPageNavBar(Page<?> Page, Model model) {
        int navStartPage = Page.getNumber() - Page.getNumber() % MAX_NAV_ITEM;
        model.addAttribute("start", navStartPage);

        int navLastPage = Math.min(navStartPage + MAX_NAV_ITEM - 1, Page.getTotalPages() - 1);
        navLastPage = Math.max(navLastPage, 0);
        model.addAttribute("last", navLastPage);

        boolean showBeforeNav = navStartPage >= MAX_NAV_ITEM;
        model.addAttribute("showBeforeNav", showBeforeNav);

        boolean showNextNav = navLastPage < Page.getTotalPages() - 1;
        model.addAttribute("showNextNav", showNextNav);
    }

    private void bindPostView(Post post, Model model) {
        model.addAttribute("post", post);
    }

    private void bindFieldError(BindingResult bindingResult, Fieldable e) {
        FieldError fieldError = new FieldError("form", e.getField(), e.getReason());
        bindingResult.addError(fieldError);
    }
}
