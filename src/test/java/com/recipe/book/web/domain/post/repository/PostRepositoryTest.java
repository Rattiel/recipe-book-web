package com.recipe.book.web.domain.post.repository;

import com.recipe.book.web.domain.post.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Transactional
    @Test()
    public void update() {
        String title = "test title";
        String content = "test content";
        List<String> thumbnails = new ArrayList<>();
        thumbnails.add("test 1");

        Long postId = postRepository.save(Post.create(title, thumbnails, content)).getId();

        postRepository.findById(postId).get().look();

        Post result = postRepository.findById(postId).get();

        Assertions.assertEquals(result.getViewsCount(), 1);
    }
}