package com.finapp.backend.security.service;

import com.finapp.backend.user.entity.User;
import com.finapp.backend.user.entity.UserPrincipal;
import com.finapp.backend.user.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist."));
        return new UserPrincipal(user);
    }
}
