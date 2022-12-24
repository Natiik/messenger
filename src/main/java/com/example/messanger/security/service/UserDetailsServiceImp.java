package com.example.messanger.security.service;

import com.example.messanger.object.UserInfo;
import com.example.messanger.security.cache.UserAuthCache;
import com.example.messanger.security.cache.UserAuthInfo;
import com.example.messanger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserService userService;
    private final UserAuthCache userAuthCache;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = userService.findUserInfoById(UUID.fromString(userId));
        if (userInfo.isEmpty()) {
            log.error("Failed to find user with id {}", userId);
            throw new UsernameNotFoundException("No user found");
        }
        UserAuthInfo userAuthInfo = userAuthCache.get(userInfo.get().getId());
        if (userAuthInfo == null) {
            log.error("Failed to get info about temporary password {}", userId);
            throw new UsernameNotFoundException("No tmpPassword found");
        }
        return new User(
                userInfo.get().getId().toString(),
                userAuthInfo.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(userInfo.get().getSubscription().toString())));
    }
}
