package com.recipe.book.web.domain.common.controller;

import com.recipe.book.web.domain.user.controller.UserController;
import com.recipe.book.web.domain.recipe.controller.RecipeController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes = {
        BaseController.class,
        UserController.class,
        RecipeController.class
})
@Controller
public class ExceptionAdviceController {
    @ExceptionHandler({
            RuntimeException.class,
            Exception.class
    })
    public String internalServerError(Exception e) {
        log.error(e.getMessage());

        return "error/500";
    }

    @ExceptionHandler({
            DataAccessException.class
    })
    public String serviceUnavailable(Exception e) {
        log.error(e.getMessage());

        return "error/503";
    }
}