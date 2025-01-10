package com.finapp.backend.service;

import com.finapp.backend.entity.User;
import com.finapp.backend.exception.SaveEntityException;
import com.finapp.backend.repository.UserRepository;
import com.finapp.backend.util.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new EntityNotFoundException(AppConstants.USER_NOT_FOUND)
        );
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(String.format("User with id %s not found.", id))
        );
    }

    @Override
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new SaveEntityException("An error occurred while trying to save user to the database", e);
        }
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
