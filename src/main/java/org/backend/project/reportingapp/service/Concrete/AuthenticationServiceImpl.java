package org.backend.project.reportingapp.service.Concrete;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public TokenResponse login(UserLoginRequest userLoginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword()));
            log.trace("User authenticated successfully: {}", userLoginRequest.getUsername());

            User user = userRepository.findByUsername(userLoginRequest.getUsername()).orElseThrow(
                    () -> {
                        log.error("User not found with username: {}", userLoginRequest.getUsername());
                        return new NullPointerException(String.format("User not found with username: %s", userLoginRequest.getUsername()));
                    });
            log.trace("User found: {}", user);

            String token = jwtService.generateToken(user);
            log.trace("Token generated for user: {}", user);

            return TokenResponse.builder().token(token).build();
        }catch (RuntimeException e) {
            log.error("Error during authentication for user: {}", userLoginRequest.getUsername());
            throw e;
        }
    }
}
