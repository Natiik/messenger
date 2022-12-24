package com.example.messanger.security.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Validator {
    private final static Pattern phonePatter = Pattern.compile("[0-9]");

    public static void validatePhoneNumber(String phoneNumber) {
        Matcher matcher = phonePatter.matcher(phoneNumber);
        if (!matcher.matches() && phoneNumber.length() != 12) {
            log.error("Phone number {} is invalid" , phoneNumber);
            throw new IllegalArgumentException("Invalid phone number");
        }
    }
}
