package com.example.messanger.controller;

import com.example.messanger.exception.MessengerAuthException;
import com.example.messanger.object.LogInUser;
import com.example.messanger.types.Subscription;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

public class BasicController {
    public LogInUser getCurrentUser() throws MessengerAuthException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new MessengerAuthException("Failed to get current user");
        }
        List<? extends GrantedAuthority> authorities =
                authentication.getAuthorities().stream().toList();
        return LogInUser.builder()
                .userId(UUID.fromString(authentication.getName()))
                .subscription(Subscription.valueOf(authorities.get(0).getAuthority()))
                .build();
    }
}
