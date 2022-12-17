package com.recipe.book.web;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.repository.RecipeRepository;
import com.recipe.book.web.domain.user.dto.UserPrincipal;
import com.recipe.book.web.global.config.security.UserRole;
import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.domain.user.dto.UserRegisterForm;
import com.recipe.book.web.domain.user.dto.UserRegisterParameter;
import com.recipe.book.web.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

@Profile("init")
@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseLoader implements ApplicationRunner {
    private final UserRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecipeRepository recipeRepository;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "1234";
    private static final String ADMIN_NICKNAME = "관리자";
    private static final String ADMIN_EMAIL = "whddlf0504@naver.com";
    private static final String THUMBNAIL_SRC = "https://www.adobe.com/kr/creativecloud/photography/hub/guides/media_1c392a98f2281a1235acca903ce9d62339fa138c1.jpeg?width=2000&format=webply&optimize=medium";


    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        Optional<User> memberOptional = memberRepository.findByUsername(ADMIN_USERNAME);

        if (memberOptional.isPresent()) {
            log.info("기존 관리자 계정 (아이디 : {}, 비밀번호 : {})", ADMIN_USERNAME, "?");
        } else {
            UserRegisterForm adminMemberRegisterForm = UserRegisterForm.builder()
                    .username(ADMIN_USERNAME)
                    .password(ADMIN_PASSWORD)
                    .nickname(ADMIN_NICKNAME)
                    .email(ADMIN_EMAIL)
                    .build();

            User admin = User.register(UserRegisterParameter.from(adminMemberRegisterForm, UserRole.ADMIN), passwordEncoder);

            memberRepository.save(admin);

            Authentication authentication = new TestingAuthenticationToken(
                    UserPrincipal.from(admin),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(UserRole.ADMIN.getId()))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            for (int i = 0; i < 150; i++) {
                Recipe recipe = Recipe.create("레시피 제목" + (i + 1), admin, Collections.singletonList(THUMBNAIL_SRC), "레시피 본문 "  + (i + 1));

                recipeRepository.save(recipe);
            }

            log.info("새로 생성된 관리자 계정 (아이디 : {}, 비밀번호 : {})", ADMIN_USERNAME, ADMIN_PASSWORD);

            User test = User.register(UserRegisterParameter.from(
                    UserRegisterForm.builder()
                            .username("test")
                            .password(ADMIN_PASSWORD)
                            .nickname("테스트 유저1")
                            .email("test@mail.com")
                            .build(),
                            UserRole.USER
                    ),
                    passwordEncoder
            );

            memberRepository.save(test);
        }
    }
}
