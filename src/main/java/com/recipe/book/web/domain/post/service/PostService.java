package com.recipe.book.web.domain.post.service;

import com.recipe.book.web.domain.post.Post;
import com.recipe.book.web.domain.post.dto.PostData;
import com.recipe.book.web.domain.post.dto.PostPreview;
import com.recipe.book.web.domain.post.exception.PostPasswordIncorrectException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Post create(String title, List<String> thumbnails,  String content);

    Post update(long id, String title, List<String> thumbnails, String content);

    void delete(long id);

    Page<PostPreview> list(Pageable pageable);

    PostData findDataById(long id);

    Post findById(long id);
}
