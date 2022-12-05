package com.recipe.book.web.domain.post.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostForm {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "썸네일을 업로드 해주세요.")
    private List<String> thumbnails;

    @NotEmpty(message = "본문을 입력해주세요.")
    private String content;

    public static PostForm from(PostData data) {
        return PostForm.builder()
                .title(data.getTitle())
                .thumbnails(data.getThumbnails())
                .content(data.getContent())
                .build();
    }
}
