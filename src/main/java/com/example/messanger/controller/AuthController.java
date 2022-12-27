package com.example.messanger.controller;

import com.example.messanger.controller.model.request.LoginRequest;
import com.example.messanger.controller.model.request.PhoneNumberRequest;
import com.example.messanger.controller.model.response.JwtToken;
import com.example.messanger.exception.SmsSendingException;
import com.example.messanger.object.UserInfo;
import com.example.messanger.security.service.AuthService;
import com.example.messanger.security.service.SmsSendingService;
import com.example.messanger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final SmsSendingService smsSendingService;

    @PostMapping("/send")
    public ResponseEntity<?> sendTemporaryPassword(@RequestBody PhoneNumberRequest request) {
        try {
            UserInfo userInfo = userService.findUserInfoOrCreateNew(request.getPhoneNumber());
            String tmpPass = authService.createTmpPassword(userInfo.getId());
            smsSendingService.sendTmpPassword(userInfo.getPhoneNumber(), tmpPass);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (SmsSendingException e) {
            return new ResponseEntity<>("Failed to send sms", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        JwtToken token = authService.authenticate(request);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        JwtToken token = authService.refreshToken(refreshToken);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
