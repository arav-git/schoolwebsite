package com.school.schoolwebsite.service;

import com.school.schoolwebsite.entity.AppUser;
import com.school.schoolwebsite.repository.UserRepository;
import java.util.Collections;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(username.trim().toLowerCase())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(
            user.getEmail(),
            user.getPassword(),
            Collections.singleton(() -> "ROLE_" + user.getRole().name())
        );
    }
}
