package com.recipe.book.web.global.config.jpa;

import com.recipe.book.web.domain.member.User;
import com.recipe.book.web.domain.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserAuditorAware implements AuditorAware<String> {
    private final UserRepository userRepository;

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User user) {
            return Optional.of(user.getUsername());
        }

        throw new AccessDeniedException("잘못된 유저 정보입니다.");
    }
}
