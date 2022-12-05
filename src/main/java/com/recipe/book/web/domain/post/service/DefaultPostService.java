package com.recipe.book.web.domain.post.service;

import com.recipe.book.web.domain.post.Post;
import com.recipe.book.web.domain.post.dto.PostData;
import com.recipe.book.web.domain.post.dto.PostPreview;
import com.recipe.book.web.domain.post.exception.PostNotFoundException;
import com.recipe.book.web.domain.post.exception.PostPasswordIncorrectException;
import com.recipe.book.web.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DefaultPostService implements PostService {
    private final PostRepository postRepository;

    @Transactional
    @Override
    public Post create(String title, List<String> thumbnails, String content) {
        Post post = Post.create(title, thumbnails, content);

        return postRepository.save(post);
    }

    @Transactional
    @Override
    public Post update(
            long id,
            String title,
            List<String> thumbnails,
            String content
    ) throws PostPasswordIncorrectException {
        Post post = find(id);

        return post.update(title, content);
    }

    @Transactional
    @Override
    public void delete(long id)
            throws PostPasswordIncorrectException {
        Post post = find(id);

        postRepository.delete(post);
    }

    @Transactional
    @Override
    public Page<PostPreview> list(Pageable pageable) {
        return postRepository.findAllPreviewBy(pageable);
    }

    @Transactional
    @Override
    public PostData findDataById(long id) {
        return postRepository.findDataById(id)
                .orElseThrow(PostNotFoundException::new);
    }

    @Transactional
    @Override
    public Post findById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        return post.look();
    }

    private Post find(long id) {
        return postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
    }
}
