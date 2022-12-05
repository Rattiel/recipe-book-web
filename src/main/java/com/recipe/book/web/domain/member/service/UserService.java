package com.recipe.book.web.domain.member.service;

import com.recipe.book.web.domain.common.exception.FieldException;
import com.recipe.book.web.domain.member.dto.UserRegisterParameter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    void register(UserRegisterParameter parameter) throws FieldException;
}
