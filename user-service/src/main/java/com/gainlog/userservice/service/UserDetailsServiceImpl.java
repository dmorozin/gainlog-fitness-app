package com.gainlog.userservice.service;

import com.gainlog.core.security.CustomUserDetails;
import com.gainlog.userservice.model.entity.Role;
import com.gainlog.userservice.model.entity.User;
import com.gainlog.userservice.repository.UserRepository;
import com.gainlog.userservice.utils.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.gainlog.userservice.utils.Constants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(ERole::name)
                .toList();

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new CustomUserDetails(user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
