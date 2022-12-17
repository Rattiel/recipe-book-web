package com.recipe.book.web.domain.comment.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentForm {
    private String content;
}
