package com.example.messanger.object;

import com.example.messanger.types.Subscription;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class LogInUser {
    UUID userId;
    Subscription subscription;
}
