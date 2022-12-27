package com.example.messanger.util;

import com.example.messanger.exception.IllegalOperationException;
import com.example.messanger.object.LogInUser;
import com.example.messanger.object.UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Validator {
    private final static Pattern phonePatter = Pattern.compile("[0-9]");

    public static void validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.startsWith("+")) {
            log.error("Phone number {} is invalid", phoneNumber);
            throw new IllegalArgumentException("Invalid phone number");
        }
        String numbers = phoneNumber.substring(1);
        Matcher matcher = phonePatter.matcher(numbers);
        if (!matcher.matches() && numbers.length() != 12) {
            log.error("Phone number {} is invalid", phoneNumber);
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    public static void validateUserInfo(LogInUser currentUser, UserInfo userInfo) {
        if (userInfo == null) {
            log.error("User Info can't be null");
            throw new IllegalArgumentException("User info can't be null");
        }
        if (userInfo.getId() == null || !currentUser.getUserId().equals(userInfo.getId())) {
            log.error("User with id {} is trying to modify user with id {}", currentUser.getUserId(), userInfo.getId());
            throw new IllegalOperationException();
        }
        if (userInfo.getPhoneNumber() == null) {
            log.error("Trying to save user {} with null phone number", userInfo.getId());
            throw new IllegalArgumentException("Phone number can't be null");
        }

        validatePhoneNumber(userInfo.getPhoneNumber());

        if (userInfo.getFirstName() == null) {
            log.error("Trying to save user {} with null first name", userInfo.getId());
            throw new IllegalArgumentException("First name can't be null");
        }
    }
}
