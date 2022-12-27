package com.example.messanger.controller;

import com.example.messanger.object.LogInUser;
import com.example.messanger.object.UserInfo;
import com.example.messanger.service.UserService;
import com.example.messanger.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController extends BasicController {
    private final UserService userService;

    @PostMapping("/user/save")
    @PreAuthorize("hasAnyAuthority('VIP', 'ORDINARY')")
    @SneakyThrows
    public ResponseEntity<?> saveUserInfo(@RequestBody UserInfo userInfo) {
        LogInUser currentUser = getCurrentUser();
        Validator.validateUserInfo(currentUser, userInfo);
        UserInfo saved = userService.saveUserInfo(userInfo);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }
}
