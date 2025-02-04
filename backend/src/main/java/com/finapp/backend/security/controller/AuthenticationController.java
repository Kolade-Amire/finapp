package com.finapp.backend.security.controller;

import com.finapp.backend.security.dto.AuthenticationRequest;
import com.finapp.backend.security.dto.AuthenticationResponse;
import com.finapp.backend.security.dto.RegistrationRequest;
import com.finapp.backend.security.dto.RegistrationResponse;
import com.finapp.backend.security.repository.AuthenticationService;
import com.finapp.backend.security.service.LogoutService;
import com.finapp.backend.utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RequestMapping(AppConstants.BASE_URL + "/auth")
@RestController
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;

    @PostMapping("/signup")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest request){
        RegistrationResponse response = authenticationService.register(request);
        URI newProfileLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(AppConstants.BASE_URL + "/user/{id}")
                .buildAndExpand(response.getUser().getId())
                .toUri();
        return ResponseEntity.created(newProfileLocation).body(authenticationService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/token-refresh/{id}")
    @PreAuthorize("T(java.util.UUID).fromString(#id) == authentication.principal.getId()")
    public void refreshToken(@PathVariable String id,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
            authenticationService.refreshAccessToken(request, response);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
    }
}
