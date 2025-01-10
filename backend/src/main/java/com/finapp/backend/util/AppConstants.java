package com.finapp.backend.util;

import org.springframework.beans.factory.annotation.Value;

public class AppConstants {

    //Security
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String AUTHORITIES = "authorities";
    public static final String USER_NOT_FOUND = "User does not exist.";
    public static final String INCORRECT_CREDENTIALS = "Invalid Email/Password, please try again.";
    public static final String ACCESS_DENIED = "You do not have permission to access this page";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String PASSWORDS_MISMATCH = "Passwords do not match!";
    public static final String REGISTRATION_SUCCESS = "User registered successfully!";
    public static final String AUTHENTICATION_SUCCESS = "User authenticated successfully!";
    public static final String REFRESH_SUCCESS = "Access token refreshed successfully!";


    //Rest API
    public static final String API_VERSION = "v1";
    public static final String BASE_URL = "/api/" + API_VERSION;
    public static final String LOGOUT_URL = BASE_URL + "/auth/logout";
    public static final String ERROR_URL = BASE_URL + "/auth/login?error";

    public static final String[] PUBLIC_URLS = {
            AppConstants.BASE_URL + "/auth/**",
            "/oauth2/**",
            AppConstants.BASE_URL + "/oauth2/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/swagger-ui/index.html",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",

    };
}
