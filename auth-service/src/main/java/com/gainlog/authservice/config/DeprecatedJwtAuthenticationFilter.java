package com.gainlog.authservice.config;

import org.springframework.stereotype.Component;

@Component
@Deprecated
public class DeprecatedJwtAuthenticationFilter {
//    private final HandlerExceptionResolver handlerExceptionResolver;
//    private final JwtUtil jwtUtil;
//    private final UserDetailsService userDetailsService;
//
//    public DeprecatedJwtAuthenticationFilter(@Qualifier("handlerExceptionResolver")
//                                             HandlerExceptionResolver handlerExceptionResolver,
//                                             JwtUtil jwtUtil,
//                                             UserDetailsService userDetailsService) {
//        this.handlerExceptionResolver = handlerExceptionResolver;
//        this.jwtUtil = jwtUtil;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
//        final String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            final String jwt = authHeader.substring(7);
//            final String userEmail = jwtUtil.extractUsername(jwt);
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (userEmail != null && authentication == null) {
//                // TODO use redis
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//
//                if (jwtUtil.isTokenValid(jwt, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities()
//                    );
//
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            }
//
//            filterChain.doFilter(request, response);
//        } catch (Exception exception) {
//            handlerExceptionResolver.resolveException(request, response, null, exception);
//        }
//    }
}