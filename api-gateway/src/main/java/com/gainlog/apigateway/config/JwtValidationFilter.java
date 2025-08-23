package com.gainlog.apigateway.config;

import com.gainlog.core.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class JwtValidationFilter implements GlobalFilter {

    private final JwtUtil jwtUtil;

    public JwtValidationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getPath().toString();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Authorization header missing or invalid");
        }

        try {
            String token = authHeader.substring(7);

            return Mono.fromCallable(() -> {
                        jwtUtil.validateToken(token);
                        return true;
                    })
                    .flatMap(valid -> chain.filter(exchange))
                    .onErrorResume(JwtException.class, ex -> unauthorized(exchange, ex.getMessage()))
                    .onErrorResume(Exception.class, ex -> unauthorized(exchange, "Unexpected error: " + ex.getMessage()));

        } catch (Exception e) {
            return unauthorized(exchange, "JWT processing failed: " + e.getMessage());
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        exchange.getResponse().setStatusCode(unauthorized);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        String body = String.format(
                "{\"timestamp\":\"%s\",\"message\":\"%s\",\"details\":\"%s\",\"status\":%d,\"error\":\"%s\"}",
                LocalDateTime.now(),
                message,
                exchange.getRequest().getPath(),
                unauthorized.value(),
                unauthorized.getReasonPhrase()
        );

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(body.getBytes(StandardCharsets.UTF_8))));
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/auth");
    }
}
