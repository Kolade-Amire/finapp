package com.finapp.backend.interfaces.service;

import com.finapp.backend.dto.auth.AuthenticationRequest;
import com.finapp.backend.dto.auth.AuthenticationResponse;
import com.finapp.backend.dto.auth.RegistrationRequest;
import com.finapp.backend.dto.auth.RegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    RegistrationResponse register(RegistrationRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
