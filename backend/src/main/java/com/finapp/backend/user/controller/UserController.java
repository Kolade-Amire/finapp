package com.finapp.backend.user.controller;

import com.finapp.backend.user.dto.UserDto;
import com.finapp.backend.user.dto.UserUpdateDto;
import com.finapp.backend.user.interfaces.UserService;
import com.finapp.backend.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(AppConstants.BASE_URL + "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read') || T(java.util.UUID).fromString(#id) == authentication.principal.getId()")
    public ResponseEntity<UserDto> retrieveUser(@PathVariable String id){
        UserDto user = userService.retrieveUser(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("T(java.util.UUID).fromString(#id) == authentication.principal.getId()")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody UserUpdateDto userDetails){
        UserDto updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("T(java.util.UUID).fromString(#id) == authentication.principal.getId()")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id){
        userService.scheduleAccountDeletion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/scheduled-for-deactivation")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Page<UserDto>> getUsersScheduledForDeletion(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "20") int pageSize) {
        Page<UserDto> users = userService.getUsersScheduledForDeletion(PageRequest.of(pageNumber, pageSize));
        return ResponseEntity.ok(users);
    }

}