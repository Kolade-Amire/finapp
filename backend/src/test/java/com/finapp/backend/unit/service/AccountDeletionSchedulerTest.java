package com.finapp.backend.unit.service;


import com.finapp.backend.user.interfaces.UserService;
import com.finapp.backend.user.service.AccountDeletionScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountDeletionSchedulerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountDeletionScheduler accountDeletionScheduler;

    private static final String EXPECTED_CRON = "0 0 0 * * ?";
    private static final String EXPECTED_ZONE = "UTC";


    @Test
    @DisplayName("Should have correct cron expression and timezone")
    void shouldHaveCorrectSchedulingConfiguration() throws NoSuchMethodException {
        // Arrange
        Class<?> schedulerClass = AccountDeletionScheduler.class;

        // Act
        Scheduled annotation =
                schedulerClass.getDeclaredMethod("deleteScheduledAccounts")
                        .getAnnotation(org.springframework.scheduling.annotation.Scheduled.class);

        // Assert
        assertNotNull(annotation, "Scheduled annotation should be present");
        assertEquals(EXPECTED_CRON, annotation.cron(), "Cron expression should match");
        assertEquals(EXPECTED_ZONE, annotation.zone(), "Timezone should be UTC");
    }

    @Test
    @DisplayName("Should successfully execute scheduled deletion")
    void shouldExecuteScheduledDeletion() {
        // Arrange
        doNothing().when(userService).deleteUsersDueForDeletion();

        // Act
        accountDeletionScheduler.deleteScheduledAccounts();

        // Assert
        verify(userService, times(1)).deleteUsersDueForDeletion();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Should be properly initialized with UserService")
    void shouldBeProperlyInitialized() {
        // Arrange & Act
        AccountDeletionScheduler newScheduler = new AccountDeletionScheduler(userService);

        // Assert
        assertNotNull(newScheduler, "Scheduler should be initialized");
        assertEquals(
                userService,
                ReflectionTestUtils.getField(newScheduler, "userService"),
                "UserService should be properly injected"
        );
    }

    @Test
    @DisplayName("Should execute at midnight UTC")
    void shouldExecuteAtMidnightUTC() {
        // Given
        String cronExpression = "0 0 0 * * ?";
        CronExpression expression = CronExpression.parse(cronExpression);

        // Test exact midnight
        LocalDateTime midnight = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime nextExecution = expression.next(midnight);

        // Next execution should be the next day at midnight
        assertEquals(
                LocalDateTime.of(2025, 1, 2, 0, 0, 0),
                nextExecution,
                "Should execute at next midnight"
        );
    }

    @Test
    @DisplayName("Should not execute at times not midnight")
    void shouldNotExecuteOutsideMidnight() {
        // Given
        String cronExpression = "0 0 0 * * ?";
        CronExpression expression = CronExpression.parse(cronExpression);

        // Test various non-midnight times
        LocalDateTime[] testTimes = {
                LocalDateTime.of(2025, 1, 1, 0, 0, 1),  // Just after midnight
                LocalDateTime.of(2025, 1, 1, 23, 59, 59),  // Just before midnight
                LocalDateTime.of(2025, 1, 1, 12, 0, 0),  // Noon
        };

        for (LocalDateTime time : testTimes) {
            LocalDateTime nextExecution = expression.next(time);

            // Should always be the next day at midnight
            assertEquals(
                    LocalDateTime.of(2025, 1, 2, 0, 0, 0),
                    nextExecution,
                    "Should not execute at " + time
            );
        }
    }
}