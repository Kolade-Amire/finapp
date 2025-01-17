package com.finapp.backend.scheduler;

import com.finapp.backend.interfaces.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class AccountDeletionScheduler {

    private final UserService userService;

    public AccountDeletionScheduler(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "UTC")
    public void deleteScheduledAccounts(){
        userService.deleteUsersDueForDeletion();
    }
}
