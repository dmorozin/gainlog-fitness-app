package com.gainlog.authservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthHandlers {

    public static class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             org.springframework.security.core.AuthenticationException authException) throws IOException {
            ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, authException.getMessage());
            pd.setProperty("description", "You must be authenticated to access this resource");
            sendJson(response, pd);
        }
    }

    public static class JwtAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response,
                           AccessDeniedException accessDeniedException) throws IOException {
            ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, accessDeniedException.getMessage());
            pd.setProperty("description", "You do not have permission to access this resource");
            sendJson(response, pd);
        }
    }

    private static void sendJson(HttpServletResponse response, ProblemDetail pd) throws IOException {
        int status = pd.getStatus();
        response.setStatus(status != 0 ? status : 500);
        response.setContentType("application/json");
        response.getWriter().write("""
                {
                  "status": %d,
                  "title": "%s",
                  "description": "%s"
                }
                """.formatted(status, pd.getTitle(), pd.getProperties().get("description")));
    }
}