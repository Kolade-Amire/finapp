package com.finapp.backend.user.interfaces;

import com.finapp.backend.user.dto.UserDto;
import com.finapp.backend.user.dto.UserUpdateDto;
import com.finapp.backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
