package com.finapp.backend.user.service;

import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.exception.SaveEntityException;
import com.finapp.backend.user.dto.UserDtoMapper;
import com.finapp.backend.user.entity.User;
import com.finapp.backend.user.dto.UserDto;
import com.finapp.backend.user.dto.UserUpdateDto;
import com.finapp.backend.user.interfaces.UserRepository;
import com.finapp.backend.user.interfaces.UserService;
import com.finapp.backend.utils.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(AppConstants.USER_NOT_FOUND)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %s not found.", id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto retrieveUser(String id) {
        try {
            UUID userId = UUID.fromString(id);
            User user = findById(userId);
            return UserDtoMapper.mapUserToDto(user);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new CustomFinAppException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        try {
           return userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new SaveEntityException("An error occurred while trying to save user.");
        }
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public UserDto updateUser(String userId, UserUpdateDto newDetails) {

        try {
            UUID id = UUID.fromString(userId);
            User existingUser = findById(id);


            //allowed updates
            if (newDetails.getFirstname() != null) {
                existingUser.setFirstname(newDetails.getFirstname());
            }
            if (newDetails.getLastname() != null) {
                existingUser.setFirstname(newDetails.getLastname());
            }
            if (newDetails.getPhoneNumber() != null) {
                existingUser.setFirstname(newDetails.getPhoneNumber());
            }
            if (newDetails.getProfilePictureUrl() != null) {
                existingUser.setFirstname(newDetails.getProfilePictureUrl());
            }

            User savedUser = saveUser(existingUser);
            return UserDtoMapper.mapUserToDto(savedUser);

        } catch (DataIntegrityViolationException e) {
            throw new CustomFinAppException("Phone number already used.");
        } catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new CustomFinAppException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void scheduleAccountDeletion(String id) {
        UUID userId = UUID.fromString(id);
        User user = findById(userId);
        user.setDeletionDate(LocalDateTime.now(ZoneOffset.UTC).plusDays(30));
        saveUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsersScheduledForDeletion(Pageable pageable) {
        Page<User> scheduledUsers = userRepository.findByDeletionDateBeforeOrderByFirstname(LocalDateTime.now(ZoneOffset.UTC), pageable);
        return UserDtoMapper.mapToPageOfDto(scheduledUsers);
    }

    @Override
    @Transactional
    public void deleteUsersDueForDeletion() {
        try {
            userRepository.deleteByDeletionDateBefore(LocalDateTime.now(ZoneOffset.UTC));
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while trying to delete accounts due for deletion", e);
        }
    }

}
