package com.example.messanger.controller;

import com.example.messanger.exception.MessengerAuthException;
import com.example.messanger.object.LogInUser;
import com.example.messanger.object.User;
import com.example.messanger.object.UserInfo;
import com.example.messanger.service.UserService;
import com.example.messanger.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController extends BasicController {
    private final UserService userService;

    @PostMapping("/userInfo/save")
    @PreAuthorize("hasAnyAuthority('VIP', 'ORDINARY')")
    public ResponseEntity<?> saveUserInfo(@RequestBody UserInfo userInfo) {
        try {
            LogInUser currentUser = getCurrentUser();
            Validator.validateUserInfo(currentUser, userInfo);
            UserInfo saved = userService.saveUserInfo(userInfo);
            return new ResponseEntity<>(saved, OK);
        } catch (MessengerAuthException e) { // todo exception handling
            log.error("Failed to authorize logged in user", e);
            return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
        }
    }

    @PostMapping("/userInfo/save")
    @PreAuthorize("hasAnyAuthority('VIP', 'ORDINARY')")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        try {
            LogInUser loginUser = getCurrentUser();
            Validator.validateUser(loginUser, user);
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, OK);
        } catch (MessengerAuthException e) { // todo exception handling
            log.error("Failed to authorize logged in user", e);
            return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
        }
    }
}
