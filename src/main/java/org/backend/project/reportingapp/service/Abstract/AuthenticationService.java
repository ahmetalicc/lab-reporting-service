package org.backend.project.reportingapp.service.Abstract;

import org.backend.project.reportingapp.dto.request.UserLoginRequest;
import org.backend.project.reportingapp.dto.request.UserRegisterRequest;
import org.backend.project.reportingapp.dto.response.TokenResponse;
import org.backend.project.reportingapp.dto.response.UserResponse;

public interface AuthenticationService {
    UserResponse saveUser(UserRegisterRequest userRegisterRequest);

    TokenResponse login(UserLoginRequest userLoginRequest);
}
