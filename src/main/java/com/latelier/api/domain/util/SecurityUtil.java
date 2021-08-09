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
            throw new UsernameNotFoundException("anonymousUser");
        }
        return Long.parseLong(id);
    }


    public Authentication getAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();
    }

}
