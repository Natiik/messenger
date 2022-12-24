package com.example.messanger.security.cache;

import lombok.Value;

@Value
public class UserAuthInfo {
    String password;
    long ts;
}
