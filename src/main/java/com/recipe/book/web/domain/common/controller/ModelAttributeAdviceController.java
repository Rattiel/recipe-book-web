package com.recipe.book.web.domain.common.controller;

import com.recipe.book.web.domain.user.controller.UserController;
import com.recipe.book.web.domain.recipe.controller.RecipeController;
import com.recipe.book.web.global.config.session.GetSessionUser;
import com.recipe.book.web.global.config.session.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@ControllerAdvice(
        assignableTypes = {
                BaseController.class,
                BaseController.class,
                UserController.class,
                RecipeController.class,
                ExceptionAdviceController.class
        }
)
@Controller
public class ModelAttributeAdviceController {
    @ModelAttribute
    public void bindMember(
            @GetSessionUser SessionUser user,
            Model model
    ) {
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

    @GetMapping("/fragment")
    public String fragment() {
        return "fragment";
    }
}
