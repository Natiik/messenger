package com.example.messanger.security;

import lombok.Value;

@Value
public class UserAuthInfo {
    String password;
    long ts;
}
