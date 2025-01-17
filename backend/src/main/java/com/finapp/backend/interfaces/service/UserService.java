package com.finapp.backend.interfaces.service;

import com.finapp.backend.domain.User;
import com.finapp.backend.dto.auth.UserDto;
import com.finapp.backend.dto.user.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserService {

    User findByEmail(String email);

    User findById(UUID id);

    UserDto retrieveUser(String id);

    UserDto saveUser(User user);

    boolean userExists(String email);

    UserDto updateUser(String id, UserUpdateDto newDetails);

    void scheduleAccountDeletion(String id);

    Page<UserDto> getUsersScheduledForDeletion(Pageable pageable);

    void deleteUsersDueForDeletion();


}
