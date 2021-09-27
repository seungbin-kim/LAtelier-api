package com.latelier.api.domain.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public Long getMemberId() {

        String id = getAuthentication().getName();
        if (id.equals("anonymousUser")) {
            throw new UsernameNotFoundException("anonymousUser"); // 인증정보가 없다면 401 발생
        }
        return Long.parseLong(id);
    }


    public boolean isLoggedIn() {

        return !getAuthentication().getName().equals("anonymousUser");
    }


    public boolean isAdmin() {

        return isLoggedIn() &&
                getAuthentication().getAuthorities().stream()
                        .map(Object::toString)
                        .anyMatch(Object -> Object.equals("ROLE_ADMIN"));
    }


    public Authentication getAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();
    }

}
