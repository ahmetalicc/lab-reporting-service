package org.backend.project.reportingapp.api;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.core.responses.DataResponse;
import org.backend.project.reportingapp.core.responses.ErrorDataResponse;
import org.backend.project.reportingapp.core.responses.SuccessDataResponse;
import org.backend.project.reportingapp.dto.request.UserLoginRequest;
import org.backend.project.reportingapp.dto.request.UserRegisterRequest;
import org.backend.project.reportingapp.dto.response.TokenResponse;
import org.backend.project.reportingapp.dto.response.UserResponse;
import org.backend.project.reportingapp.service.Abstract.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/save")
    public ResponseEntity<DataResponse<UserResponse>> saveUser(@RequestBody UserRegisterRequest userRegisterRequest){
        try {
            return ResponseEntity.ok(new SuccessDataResponse<>(authenticationService.saveUser(userRegisterRequest), "User is saved to the database successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDataResponse<>(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<DataResponse<TokenResponse>> login(@Valid @RequestBody UserLoginRequest userLoginRequest){
        try {
            return ResponseEntity.ok(new SuccessDataResponse<>(authenticationService.login(userLoginRequest), "Login successful."));
        }catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDataResponse<>(e.getMessage()));
        }
    }
}
