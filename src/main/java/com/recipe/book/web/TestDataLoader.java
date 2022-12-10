package com.recipe.book.web;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recipe.repository.RecipeRepository;
import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.domain.user.dto.UserPrincipal;
import com.recipe.book.web.domain.user.dto.UserRegisterForm;
import com.recipe.book.web.domain.user.dto.UserRegisterParameter;
import com.recipe.book.web.domain.user.repository.UserRepository;
import com.recipe.book.web.global.config.security.UserRole;
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
public class TestDataLoader implements ApplicationRunner {
    private final UserRepository memberRepository;
    private static final String ADMIN_USERNAME = "admin";
    private static final String THUMBNAIL_SRC = "https://www.adobe.com/kr/creativecloud/photography/hub/guides/media_1c392a98f2281a1235acca903ce9d62339fa138c1.jpeg?width=2000&format=webply&optimize=medium";

    private final RecipeRepository recipeRepository;

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        User admin = memberRepository.findByUsername(ADMIN_USERNAME).get();

        Authentication authentication = new TestingAuthenticationToken(
                UserPrincipal.from(admin),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(UserRole.ADMIN.getId()))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        for (int i = 0; i < 60; i++) {
            Recipe recipe = Recipe.create("레시피 제목" + (i + 1), admin, Collections.singletonList(THUMBNAIL_SRC), "레시피 본문 "  + (i + 1));

            recipeRepository.save(recipe);
        }
    }
}
