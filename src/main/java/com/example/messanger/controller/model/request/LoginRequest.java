package com.example.messanger.controller.model.request;

import lombok.Value;

@Value
public class LoginRequest {
    String phoneNumber;
    String password;
}
