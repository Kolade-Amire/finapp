package com.finapp.backend.user.unit;

import com.finapp.backend.user.entity.User;
import com.finapp.backend.user.dto.UserDto;
import com.finapp.backend.user.dto.UserUpdateDto;
import com.finapp.backend.user.interfaces.UserRepository;
import com.finapp.backend.security.enums.Role;
import com.finapp.backend.user.service.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    private static final String TEST_EMAIL = "testEmail@finapp.com";
    private static final String TEST_PASSWORD = "Password123!";
    private static final String TEST_FIRSTNAME = "Frank";
    private static final String TEST_LASTNAME = "Ocean";
    private static final String TEST_PHONE = "+1234567890";
    private static final String PROFILE_PIC_URL = "https://picsum.photos/200";

    @BeforeEach
    void setup() {
        // Setup test user
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .firstname(TEST_FIRSTNAME)
                .lastname(TEST_LASTNAME)
                .phoneNumber(TEST_PHONE)
                .role(Role.USER)
                .isEmailVerified(true)
                .isAccountExpired(false)
                .profilePictureUrl(PROFILE_PIC_URL)
                .createdAt(LocalDateTime.now().minusDays(3))
                .lastLoginAt(LocalDateTime.now().minusMinutes(5))
                .build();
    }

    @Nested
    @DisplayName("User Retrieval Tests")
    class UserRetrievalTests {

        @Test
        @DisplayName("Should find user by email")
        void shouldFindUserByEmail() {

            //Arrange
            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.ofNullable(testUser));
            //Act
            User returnedUser = userService.findByEmail(TEST_EMAIL);
            //Assert
            assertNotNull(returnedUser);
            verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
            assertThat(returnedUser)
                    .hasFieldOrPropertyWithValue("email", TEST_EMAIL)
                    .hasFieldOrPropertyWithValue("firstname", TEST_FIRSTNAME)
                    .hasFieldOrPropertyWithValue("role", Role.USER)
                    .hasFieldOrPropertyWithValue("phoneNumber", TEST_PHONE);

        }

        @Test
        @DisplayName("Should find user by ID")
        void shouldFindUserById() {
            //Arrange
            var testId = UUID.randomUUID();
            when(userRepository.findById(testId)).thenReturn(Optional.ofNullable(testUser));
            //Act
            User returnedUser = userService.findById(testId);
            //Assert
            assertNotNull(returnedUser);
            verify(userRepository, times(1)).findById(testId);
            assertThat(returnedUser)
                    .hasFieldOrPropertyWithValue("id", testUser.getId())
                    .hasFieldOrPropertyWithValue("email", TEST_EMAIL)
                    .hasFieldOrPropertyWithValue("firstname", TEST_FIRSTNAME)
                    .hasFieldOrPropertyWithValue("role", Role.USER)
                    .hasFieldOrPropertyWithValue("phoneNumber", TEST_PHONE);

        }

        @Test
        @DisplayName("Should find user by ID and return User DTO")
        void shouldRetrieveUserAndReturnDto() {
            //Arrange
            var testId = UUID.randomUUID();
            when(userRepository.findById(testId)).thenReturn(Optional.ofNullable(testUser));
            //Act
            UserDto returnedUser = userService.retrieveUser(testId.toString());
            //Assert
            assertNotNull(returnedUser);
            verify(userRepository, times(1)).findById(testId);
            assertThat(returnedUser)
                    .hasFieldOrPropertyWithValue("id", testUser.getId().toString())
                    .hasFieldOrPropertyWithValue("email", TEST_EMAIL)
                    .hasFieldOrPropertyWithValue("firstname", TEST_FIRSTNAME)
                    .hasFieldOrPropertyWithValue("role", Role.USER)
                    .hasFieldOrPropertyWithValue("phoneNumber", TEST_PHONE);

        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when user doesn't exist and FindById is called.")
        void findByIdShouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {

            //Arrange
            var testId = UUID.randomUUID();
            when(userRepository.findById(testId)).thenReturn(Optional.empty());

            //Act & Assert
            assertThrows(EntityNotFoundException.class, () -> userService.findById(testId));

            verify(userRepository, times(1)).findById(testId);

        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when user doesn't exist and FindById is called.")
        void findByEmailShouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {

            //Arrange
            String invalidEmail = "invalidemail@notexist.com";
            when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());

            //Act & Assert
            assertThrows(EntityNotFoundException.class, () -> userService.findByEmail(invalidEmail));

            verify(userRepository, times(1)).findByEmail(invalidEmail);

        }

        @Test
        @DisplayName("Should return false when user doesn't exist")
        void shouldReturnFalseWhenUserDoesNotExist() {
            //Arrange
            String newEmail = "email@does-not-exist.com";
            when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());
            //Act
            boolean userExists = userService.userExists(newEmail);
            //Assert
            assertFalse(userExists);
            verify(userRepository, times(1)).findByEmail(newEmail);

        }

    }

    @Nested
    class UserStorageTests {
        @Test
        @DisplayName("Should save user and return DTO")
        void shouldSaveUserAndReturnDto() {
            //Arrange
            when(userRepository.save(testUser)).thenReturn(testUser);
            //Act
            User savedUser = userService.saveUser(testUser);
            //Assert
            assertNotNull(savedUser);
            verify(userRepository, times(1)).save(testUser);
            assertThat(savedUser)
                    .hasFieldOrPropertyWithValue("id", testUser.getId().toString())
                    .hasFieldOrPropertyWithValue("firstname", testUser.getFirstname())
                    .hasFieldOrPropertyWithValue("createdAt", testUser.getCreatedAt())
            ;

        }

        @Test
        @DisplayName("Should update existing user and return DTO")
        void shouldUpdateExistingUserAndReturnDto() {
            //Arrange
            UserUpdateDto newUserDetails = UserUpdateDto.builder()
                    .firstname("new-first-name")
                    .lastname("new-last-name")
                    .phoneNumber("+2349090909090")
                    .profilePictureUrl("newProfilePhoto")
                    .build();

            User updatedUser = User.builder()
                    .id(testUser.getId())
                    .email(TEST_EMAIL)
                    .password(TEST_PASSWORD)
                    .firstname(newUserDetails.getFirstname())
                    .lastname(newUserDetails.getLastname())
                    .phoneNumber(newUserDetails.getPhoneNumber())
                    .role(Role.USER)
                    .isEmailVerified(true)
                    .isAccountExpired(false)
                    .profilePictureUrl(newUserDetails.getProfilePictureUrl())
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .lastLoginAt(LocalDateTime.now().minusMinutes(5))
                    .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                    .build();

            UUID existingUserId = testUser.getId();
            when(userRepository.findById(existingUserId)).thenReturn(Optional.ofNullable(testUser));
            when(userRepository.save(testUser)).thenReturn(updatedUser);
            //Act
            UserDto savedUserDto = userService.updateUser(testUser.getId().toString(), newUserDetails);
            //Assert
            assertNotNull(savedUserDto);
            verify(userRepository, times(1)).findById(existingUserId);
            verify(userRepository, times(1)).save(testUser);
            assertThat(savedUserDto)
                    .hasFieldOrPropertyWithValue("id", updatedUser.getId().toString())
                    .hasFieldOrPropertyWithValue("firstname", updatedUser.getFirstname())
                    .hasFieldOrPropertyWithValue("lastname", updatedUser.getLastname())
                    .hasFieldOrPropertyWithValue("phoneNumber", updatedUser.getPhoneNumber())
                    .hasFieldOrPropertyWithValue("profilePicture", updatedUser.getProfilePictureUrl())
                    .hasFieldOrPropertyWithValue("updatedAt", updatedUser.getUpdatedAt())
            ;
        }
    }


    @Nested
    @DisplayName("User Deletion Tests")
    class UserDeletionTests {

        @Captor
        private ArgumentCaptor<User> userCaptor;

        @Captor
        private ArgumentCaptor<LocalDateTime> localDateTimeCaptor;

        @Test
        @DisplayName("Should schedule account deletion for user")
        void shouldScheduleAccountDeletionForUser() {

            //Arrange
            when(userRepository.findById(testUser.getId())).thenReturn(Optional.ofNullable(testUser));
            when(userRepository.save(testUser)).thenReturn(testUser);
            String id = testUser.getId().toString();

            //Act
            userService.scheduleAccountDeletion(id);

            //Assert
            verify(userRepository, times(1)).save(userCaptor.capture());
            verify(userRepository, times(1)).findById(testUser.getId());

            User capturedUser = userCaptor.getValue();

            assertNotNull(capturedUser.getDeletionDate());
            assertEquals(LocalDateTime.now(ZoneOffset.UTC).plusDays(30).toLocalDate(), capturedUser.getDeletionDate().toLocalDate());
        }

        @Test
        void getUsersScheduledForDeletion_ShouldReturnPageOfUserDtos() {
            // Arrange
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            User user1 = createTestUser("Hailey", "Williams", now.minusDays(1));
            User user2 = createTestUser("Kanye", "West", now.minusDays(2));

            List<User> users = List.of(user1, user2);
            Page<User> userPage = new PageImpl<>(users);
            Pageable pageable = PageRequest.of(0, 10);

            when(userRepository.findByDeletionDateBeforeOrderByFirstname(any(LocalDateTime.class), eq(pageable)))
                    .thenReturn(userPage);

            // Act
            Page<UserDto> result = userService.getUsersScheduledForDeletion(pageable);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            verify(userRepository, times(1)).findByDeletionDateBeforeOrderByFirstname(localDateTimeCaptor.capture(), eq(pageable));

            // Verify UserDto mapping
            UserDto firstUserDto = result.getContent().get(0);
            assertEquals(user1.getFirstname(), firstUserDto.getFirstname());
            assertEquals(user1.getLastname(), firstUserDto.getLastname());
            assertEquals(user1.getEmail(), firstUserDto.getEmail());
        }

        @Test
        void getUsersScheduledForDeletion_ShouldReturnEmptyPage_WhenNoUsersFound() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            when(userRepository.findByDeletionDateBeforeOrderByFirstname(any(LocalDateTime.class), eq(pageable)))
                    .thenReturn(new PageImpl<>(List.of()));

            // Act
            Page<UserDto> result = userService.getUsersScheduledForDeletion(pageable);

            // Assert
            assertNotNull(result);
            assertTrue(result.getContent().isEmpty());
            assertEquals(0, result.getTotalElements());
        }

        @Test
        void deleteUsersDueForDeletion_ShouldDeleteUsers() {
            // Arrange
            doNothing().when(userRepository).deleteByDeletionDateBefore(any(LocalDateTime.class));

            // Act
            userService.deleteUsersDueForDeletion();

            // Assert
            verify(userRepository, times(1)).deleteByDeletionDateBefore(localDateTimeCaptor.capture());
            LocalDateTime capturedDateTime = localDateTimeCaptor.getValue();
            assertNotNull(capturedDateTime);

            // Verify the time is in UTC and close to now
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            assertTrue(capturedDateTime.isAfter(now.minusMinutes(1)));
            assertTrue(capturedDateTime.isBefore(now.plusMinutes(1)));
        }


        private User createTestUser(String firstName, String lastName, LocalDateTime deletionDate) {
            return User.builder()
                    .id(UUID.randomUUID())
                    .firstname(firstName)
                    .lastname(lastName)
                    .email(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com")
                    .phoneNumber("+1234567890")
                    .role(Role.USER)
                    .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                    .deletionDate(deletionDate)
                    .isAccountLocked(false)
                    .isAccountExpired(false)
                    .isEmailVerified(true)
                    .build();
        }


    }


}
