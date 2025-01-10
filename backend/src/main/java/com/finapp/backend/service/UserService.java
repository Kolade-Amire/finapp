package com.finapp.backend.service;

import com.finapp.backend.entity.User;

import java.util.UUID;

public interface UserService {

    User findByEmail(String email);

    User findById(UUID id);

    User saveUser(User user);

    boolean userExists(String email);
}
