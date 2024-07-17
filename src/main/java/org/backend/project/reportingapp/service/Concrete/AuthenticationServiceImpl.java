package org.backend.project.reportingapp.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.dao.UserRepository;
import org.backend.project.reportingapp.dto.request.UserLoginRequest;
import org.backend.project.reportingapp.dto.response.TokenResponse;
import org.backend.project.reportingapp.entity.User;
import org.backend.project.reportingapp.security.JwtService;
import org.backend.project.reportingapp.service.Abstract.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public TokenResponse login(UserLoginRequest userLoginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword()));

        User user = userRepository.findByUsername(userLoginRequest.getUsername()).orElseThrow(
                () -> new NullPointerException(String.format("User not found with username: %s", userLoginRequest.getUsername())));

        String token = jwtService.generateToken(user);

        return TokenResponse.builder().token(token).build();
    }
}
