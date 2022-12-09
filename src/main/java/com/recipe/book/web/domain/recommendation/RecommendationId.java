package com.recipe.book.web.domain.recommendation;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RecommendationId implements Serializable {
    private User owner;
    private Recipe recipe;
}