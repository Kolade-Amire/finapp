package com.finapp.backend.security.repository;

import com.finapp.backend.security.dto.AuthenticationRequest;
import com.finapp.backend.security.dto.AuthenticationResponse;
import com.finapp.backend.security.dto.RegistrationRequest;
import com.finapp.backend.security.dto.RegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    RegistrationResponse register(RegistrationRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
