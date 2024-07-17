package org.backend.project.reportingapp.service.Abstract;

import org.backend.project.reportingapp.dto.request.UserLoginRequest;
import org.backend.project.reportingapp.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse login(UserLoginRequest userLoginRequest);
}
