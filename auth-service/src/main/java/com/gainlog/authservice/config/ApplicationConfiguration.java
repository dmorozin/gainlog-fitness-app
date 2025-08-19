package com.gainlog.authservice.config;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

//    private final UserServiceGrpcClient userServiceGrpcClient;

//    @Bean
//    UserDetailsService userDetailsService() {
//        return username -> {
//            UserProto.UserResponse userResponse;
//            try {
//                userResponse = userServiceGrpcClient.getUserByEmail(username);
//            } catch (Exception e) {
//                throw new UsernameNotFoundException("User not found via gRPC: " + username, e);
//            }
//
//            if (userResponse == null || userResponse.getEmail().isEmpty()) {
//                throw new UsernameNotFoundException("User not found: " + username);
//            }
//            List<GrantedAuthority> authorities = userResponse.getRolesList().stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());
//            return new CustomUserDetails(userResponse.getId(),
//                    userResponse.getEmail(),
//                    userResponse.getPassword(),
//                    authorities);
//        };
//    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }

    @Bean
    public GrpcAuthenticationReader grpcAuthenticationReader() {
        return new BasicGrpcAuthenticationReader();
    }

}