package org.backend.project.reportingapp.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.dao.RoleRepository;
import org.backend.project.reportingapp.dao.UserRepository;
import org.backend.project.reportingapp.dto.request.UserLoginRequest;
import org.backend.project.reportingapp.dto.request.UserRegisterRequest;
import org.backend.project.reportingapp.dto.response.TokenResponse;
import org.backend.project.reportingapp.dto.response.UserResponse;
import org.backend.project.reportingapp.entity.Role;
import org.backend.project.reportingapp.entity.User;
import org.backend.project.reportingapp.security.JwtService;
import org.backend.project.reportingapp.service.Abstract.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    @Override
    public UserResponse saveUser(UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest.getRoleIds() == null || userRegisterRequest.getRoleIds().isEmpty()) {
            throw new IllegalArgumentException("RoleId list cannot be null or empty");
        }

        List<Role> roles = userRegisterRequest.getRoleIds().stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("No roles found for the provided RoleId list");
        }


        User user = User.builder()
                .name(userRegisterRequest.getName())
                .username(userRegisterRequest.getUsername())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .roles(roles).build();

        userRepository.save(user);

        return UserResponse.Convert(user);
    }

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
