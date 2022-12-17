package com.recipe.book.web.domain.comment.repository;

import com.recipe.book.web.domain.comment.Comment;
import com.recipe.book.web.global.config.security.access.AccessOwnerAndAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @AccessOwnerAndAdmin
    @Override
    Optional<Comment> findById(Long id);
}
