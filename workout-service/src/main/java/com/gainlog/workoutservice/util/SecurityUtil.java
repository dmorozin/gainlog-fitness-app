package com.gainlog.workoutservice.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Long getUserId() {
        return Long.valueOf(SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString());

    }
}
