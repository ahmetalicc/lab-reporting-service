package org.backend.project.reportingapp.config;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.entity.User;
import org.backend.project.reportingapp.dao.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new NullPointerException("User not found with username:"+ username));
        return new CustomUserDetails(user);
    }
}