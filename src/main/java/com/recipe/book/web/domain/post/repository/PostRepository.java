package com.recipe.book.web.domain.post.repository;

import com.recipe.book.web.domain.post.Post;
import com.recipe.book.web.domain.post.dto.PostData;
import com.recipe.book.web.domain.post.dto.PostPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<PostPreview> findAllPreviewBy(Pageable pageable);

    Optional<PostData> findDataById(Long id);
}
