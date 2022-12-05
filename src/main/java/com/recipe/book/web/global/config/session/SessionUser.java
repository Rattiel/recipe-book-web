package com.recipe.book.web.global.config.session;

import com.recipe.book.web.domain.member.User;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class SessionUser implements Serializable {
    public final static String ATTRIBUTE_NAME = "user";

    private Long id;
    private String username;
    private String nickname;

    public static SessionUser of(User user) {
        return SessionUser.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                .build();
    }
}
