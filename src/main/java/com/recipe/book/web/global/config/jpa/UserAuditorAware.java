package com.recipe.book.web.global.config.jpa;

import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.domain.user.dto.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class UserAuditorAware implements AuditorAware<User> {
    @Transactional
    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            return Optional.of(User.from(userPrincipal));
        }

        throw new AccessDeniedException("잘못된 유저 정보입니다.");
    }
}

