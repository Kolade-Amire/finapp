package com.finapp.backend.interfaces.service;

import com.finapp.backend.domain.User;
import com.finapp.backend.dto.auth.UserDto;
import com.finapp.backend.dto.user.UserUpdateDto;

import java.util.UUID;

public interface UserService {

    User findByEmail(String email);

    User findById(UUID id);

    UserDto retrieveUser(String id);

    UserDto saveUser(User user);

    boolean userExists(String email);

    UserDto updateUser(String id, UserUpdateDto newDetails);

    void deleteUser(String id);

}
