package com.finapp.backend.interfaces.service;

import com.finapp.backend.dto.AuthenticationRequest;
import com.finapp.backend.dto.AuthenticationResponse;
import com.finapp.backend.dto.RegistrationRequest;
import com.finapp.backend.dto.RegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    RegistrationResponse register(RegistrationRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
