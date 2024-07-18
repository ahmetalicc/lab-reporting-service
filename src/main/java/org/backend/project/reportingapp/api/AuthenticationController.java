package org.backend.project.reportingapp.api;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.core.responses.DataResponse;
import org.backend.project.reportingapp.core.responses.ErrorDataResponse;
import org.backend.project.reportingapp.core.responses.SuccessDataResponse;
import org.backend.project.reportingapp.dto.request.UserLoginRequest;
import org.backend.project.reportingapp.dto.response.TokenResponse;
import org.backend.project.reportingapp.service.Abstract.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/login")
    public ResponseEntity<DataResponse<TokenResponse>> login(@Valid @RequestBody UserLoginRequest userLoginRequest){
        try {
            return ResponseEntity.ok(new SuccessDataResponse<>(authenticationService.login(userLoginRequest), "Login successful."));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDataResponse<>(e.getMessage()));
        }
    }
}
