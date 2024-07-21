package org.backend.project.reportingapp.service.concrete;

import org.backend.project.reportingapp.dao.UserRepository;
import org.backend.project.reportingapp.dto.request.UserLoginRequest;
import org.backend.project.reportingapp.dto.response.TokenResponse;
import org.backend.project.reportingapp.entity.User;
import org.backend.project.reportingapp.fixture.UserFixture;
import org.backend.project.reportingapp.security.JwtService;
import org.backend.project.reportingapp.service.Concrete.AuthenticationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @DisplayName("The test when call with valid user information that should return JWT Token with TokenResponse")
    @Test
    void auth_whenCallWithValidUserInformation_ShouldReturnJwtTokenWithTokenResponse(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("test", "password");
        UserFixture userFixture = new UserFixture();
        User user = userFixture.createUser();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mocked_token");

        TokenResponse tokenResponse = authenticationService.login(userLoginRequest);

        assertNotNull(tokenResponse);
        assertEquals("mocked_token", tokenResponse.getToken());
        verify(userRepository, times(1)).findByUsername(anyString());
    }
    @DisplayName("The test when user not found should throw NullPointerExceptionException")
    @Test
    void auth_whenUserNotFound_ShouldThrowNullPointerExceptionException() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("non_existing_user", "password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(()-> authenticationService.login(userLoginRequest))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(String.format("User not found with username: %s", userLoginRequest.getUsername()));
    }

    @DisplayName("The test when authentication fails should throw RuntimeException")
    @Test
    void auth_whenAuthenticationFails_ShouldThrowRuntimeException() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("test", "invalid_password");

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Authentication failed"));

        assertThrows(RuntimeException.class, () -> authenticationService.login(userLoginRequest));
        verify(authenticationManager, times(1)).authenticate(any());
    }
}
