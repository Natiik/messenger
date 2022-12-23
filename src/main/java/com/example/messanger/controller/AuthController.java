package com.example.messanger.controller;

import com.example.messanger.controller.model.request.LoginRequest;
import com.example.messanger.controller.model.response.JwtToken;
import com.example.messanger.dao.UserDaoService;
import com.example.messanger.object.UserInfo;
import com.example.messanger.security.*;
import com.example.messanger.types.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    @Value("${messenger.tmp-password-ttl}")
    private long passwordTtl;
    private final JwtUtils jwtUtils;

    //    private final AuthenticationManager authenticationManager;
    private final UserAuthCache userAuthCache;
    private final UserDaoService userDaoService; // todo replace with another service

    @PostMapping("/send")
    public ResponseEntity<?> sendTemporaryPassword(@RequestParam String phoneNumber) {
        try {
            Validator.validatePhoneNumber(phoneNumber);
            UserInfo userInfo = getUserInfo(phoneNumber);
            String tmpPass = userAuthCache.put(userInfo.getId());
            return new ResponseEntity<>(tmpPass, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        long currentTs = System.currentTimeMillis();
        Optional<UserInfo> info = userDaoService.findUserByPhoneNumber(request.getPhoneNumber());
        if (info.isEmpty()) {
            log.error("Unknown user is trying to login with phoneNumber {}", request.getPhoneNumber());
            return new ResponseEntity<>("Unknown phoneNumber", HttpStatus.NOT_FOUND);
        }
        UserInfo userInfo = info.get();
        UserAuthInfo authInfo = userAuthCache.get(userInfo.getId());
        if (authInfo == null || (currentTs - authInfo.getTs()) > passwordTtl) {
            log.error("Failed to authenticate user with phone number {} ", request.getPhoneNumber());
            return new ResponseEntity<>("Password has expired", HttpStatus.UNAUTHORIZED);
        }

        User userDetails = new User(userInfo.getId().toString(), authInfo.getPassword(), Collections.singleton(new SimpleGrantedAuthority(userInfo.getSubscription().toString())));
        String jwtToken = jwtUtils.generateToken(userDetails, new HashMap<>());
        return new ResponseEntity<>(new JwtToken(jwtToken), HttpStatus.OK);
    }

    private UserInfo getUserInfo(String phoneNumber) {
        Optional<UserInfo> userByPhoneNumber = userDaoService.findUserByPhoneNumber(phoneNumber);
        if (userByPhoneNumber.isPresent()) {
            return userByPhoneNumber.get();
        }
        UserInfo newUserInfo = UserInfo.builder()
                .phoneNumber(phoneNumber)
                .subscription(Subscription.ORDINARY)
                .firstName("user"+ NumericStringUtils.generateNumbericString(8))
                .build();
        return userDaoService.saveUserInfo(newUserInfo);
    }
}
