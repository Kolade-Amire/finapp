package com.finapp.backend.security.listener;

import com.finapp.backend.security.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;

    @Override
    public void onApplicationEvent(@NonNull AuthenticationFailureBadCredentialsEvent event) {
        String ip = getClientIP();
        loginAttemptService.loginFailed(ip);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
