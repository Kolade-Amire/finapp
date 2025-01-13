package com.finapp.backend.interfaces.service;

import com.finapp.backend.domain.User;

import java.util.UUID;

public interface UserService {

    User findByEmail(String email);

    User findById(UUID id);

    User saveUser(User user);

    boolean userExists(String email);
}
