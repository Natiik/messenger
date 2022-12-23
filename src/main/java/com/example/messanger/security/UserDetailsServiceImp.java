package com.example.messanger.security;

import com.example.messanger.dao.UserDaoService;
import com.example.messanger.object.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserDaoService userDaoService;
    private final UserAuthCache userAuthCache;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserInfo userInfo = userDaoService.findUserInfoById(UUID.fromString(userId));
        if (userInfo.getId() == null) {
            log.error("Failed to find user with id {}", userId);
            throw new UsernameNotFoundException("No user found");
        }
        UserAuthInfo userAuthInfo = userAuthCache.get(userInfo.getId());
        if(userAuthInfo == null){
            log.error("Failed to get info about temporary password {}", userId);
            throw new UsernameNotFoundException("No tmpPassword found");
        }
        return new User(
                userInfo.getId().toString(),
                userAuthInfo.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(userInfo.getSubscription().toString())));
    }
}
