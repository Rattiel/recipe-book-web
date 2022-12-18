package com.recipe.book.web.global.config.security;

import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.domain.user.dto.UserPrincipal;
import com.recipe.book.web.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityUtil {
    private final UserRepository userRepository;

    public User getUser() {
        UserDetails userDetails = getUserPrincipal();

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new AccessDeniedException("사용자 정보 불러오기 실패(이유: 존재하지 않는 사용자)"));
    }

    public boolean checkEditable(Ownable data) {
        try {
            UserPrincipal userPrincipal = getUserPrincipal();

            if (userPrincipal.getRole().equals(UserRole.ADMIN)) {
                return true;
            } else {
                return data.getPrincipalName().equals(userPrincipal.getUsername());
            }
        } catch (AccessDeniedException e) {
            return false;
        }
    }

    private UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("사용자 정보 불러오기 실패(이유: 인증 안됌)");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal;
        }

        throw new ClassCastException("사용자 아이디 가져오기 실패(이유: UserPrincipal 로 형변환 실패)");
    }
}
