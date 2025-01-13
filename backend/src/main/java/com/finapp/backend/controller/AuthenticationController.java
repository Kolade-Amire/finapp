package com.finapp.backend.controller;

import com.finapp.backend.dto.AuthenticationRequest;
import com.finapp.backend.dto.AuthenticationResponse;
import com.finapp.backend.dto.RegistrationRequest;
import com.finapp.backend.dto.RegistrationResponse;
import com.finapp.backend.security.service.LogoutService;
import com.finapp.backend.service.AuthenticationServiceImpl;
import com.finapp.backend.utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping(AppConstants.BASE_URL + "/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;
    private final LogoutService logoutService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request){
        return ResponseEntity.ok(authenticationServiceImpl.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationServiceImpl.authenticate(request));
    }

    @PostMapping("/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
            authenticationServiceImpl.refreshAccessToken(request, response);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
    }
}
