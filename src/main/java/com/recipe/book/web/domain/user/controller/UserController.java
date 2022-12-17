package com.recipe.book.web.domain.user.controller;

import com.recipe.book.web.domain.common.exception.FieldException;
import com.recipe.book.web.domain.common.exception.Fieldable;
import com.recipe.book.web.domain.user.dto.UserRegisterParameter;
import com.recipe.book.web.domain.user.dto.UserRegisterForm;
import com.recipe.book.web.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String renderLoginPage(
            @RequestParam(name = "redirectUrl", defaultValue = "/") String ignoredRedirectUrl
    ) {
        return "user/login";
    }

    @GetMapping("/register")
    public String renderRegisterPage(
            @ModelAttribute("form") UserRegisterForm registerForm
    ) {
        return "user/register";
    }

    @PostMapping("/register")
    public String requestRegister(
            @Valid @ModelAttribute("form") UserRegisterForm registerForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        try {
            userService.register(UserRegisterParameter.from(registerForm));
        } catch (FieldException e) {
            bindFieldError(bindingResult, e);

            return "user/register";
        }

        return "redirect:/login";
    }

    private void bindFieldError(BindingResult bindingResult, Fieldable e) {
        FieldError fieldError = new FieldError("form", e.getField(), e.getReason());
        bindingResult.addError(fieldError);
    }
}
