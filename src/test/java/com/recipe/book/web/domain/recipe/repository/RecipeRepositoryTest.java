package com.recipe.book.web.domain.recipe.repository;

import com.recipe.book.web.domain.recipe.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Transactional
    @Test()
    public void update() {
        String title = "test title";
        String content = "test content";
        List<String> thumbnails = new ArrayList<>();
        thumbnails.add("test 1");

        Long postId = recipeRepository.save(Recipe.create(title, thumbnails, content)).getId();

        recipeRepository.findById(postId).get().look();

        Recipe result = recipeRepository.findById(postId).get();

        Assertions.assertEquals(result.getViews(), 1);
    }
}