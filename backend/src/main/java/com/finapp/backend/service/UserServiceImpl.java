package com.finapp.backend.service;

import com.finapp.backend.entity.User;
import com.finapp.backend.repository.UserRepository;
import com.finapp.backend.util.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserByEmail (String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new EntityNotFoundException(AppConstants.USER_NOT_FOUND)
        );
    }
}
