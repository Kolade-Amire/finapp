package com.finapp.backend.controller;

import com.finapp.backend.dto.auth.UserDto;
import com.finapp.backend.interfaces.service.UserService;
import com.finapp.backend.utils.AppConstants;
import lombok.RequiredArgsConstructor;
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

    @PatchMapping
    public ResponseEntity<UserDto> updateUser(){

    }



}