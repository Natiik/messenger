package com.example.messanger.security.service;

import com.example.messanger.controller.model.request.LoginRequest;
import com.example.messanger.controller.model.response.JwtToken;
import com.example.messanger.exception.InvalidJwtTokenException;
import com.example.messanger.object.UserInfo;
import com.example.messanger.security.cache.UserAuthCache;
import com.example.messanger.security.cache.UserAuthInfo;
import com.example.messanger.security.util.JwtUtils;
import com.example.messanger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    @Value("${messenger.tmp-password-ttl}")
    private long passwordTtl;
    private final UserService userService;
    private final UserAuthCache userAuthCache;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public JwtToken authenticate(LoginRequest request) throws AuthenticationException {
        long currentTs = System.currentTimeMillis();
        Optional<UserInfo> info = userService.findUserInfoByPhoneNumber(request.getPhoneNumber());

        if (info.isEmpty()) {
            log.error("Unknown user is trying to login with phoneNumber {}", request.getPhoneNumber());
            throw new UsernameNotFoundException("Unknown user phone number");
        }

        UserInfo userInfo = info.get();
        UserAuthInfo authInfo = userAuthCache.get(userInfo.getId());
        userAuthCache.evict(userInfo.getId());

        if (authInfo == null) {
            log.error("Failed to find auth info for user with pn {}", request.getPhoneNumber());
            throw new InternalAuthenticationServiceException("Unknown phone number");
        } else if (!authInfo.getPassword().equals(request.getPassword())) {
            log.error("Invalid password for user with pn {}", request.getPhoneNumber());
            throw new BadCredentialsException("Invalid password");
        } else if ((currentTs - authInfo.getTs()) > passwordTtl) {
            log.error("Failed to authenticate user with phone number {} ", request.getPhoneNumber());
            throw new CredentialsExpiredException("Tmp password is expired");
        }

        User user = new User(
                userInfo.getId().toString(),
                authInfo.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(userInfo.getSubscription().toString()))
        );

        String jwtToken = jwtUtils.generateToken(user, new HashMap<>());
        String refreshToken = jwtUtils.generateRefreshToken(user, new HashMap<>());
        return new JwtToken(jwtToken, refreshToken);
    }

    public JwtToken refreshToken(String oldRefreshToken) {
        String userId = jwtUtils.extractUsername(oldRefreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        if (jwtUtils.isTokenValid(oldRefreshToken, userDetails)) {
            String jwtToken = jwtUtils.generateToken(userDetails, new HashMap<>());
            String refreshToken = jwtUtils.generateRefreshToken(userDetails, new HashMap<>());
            return new JwtToken(jwtToken, refreshToken);
        }
        log.error("Provided refresh token is invalid for user {}", userId);
        throw new InvalidJwtTokenException();
    }

    public String createTmpPassword(UUID userId) {
        return userAuthCache.put(userId);
    }
}
