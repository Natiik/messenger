package com.example.messanger.security.cache;

import com.example.messanger.util.NumericStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class UserAuthCache {
    private final ConcurrentMap<UUID, UserAuthInfo> authInfos = new ConcurrentHashMap<>();

    public String put(UUID userId){
        UserAuthInfo info = new UserAuthInfo(NumericStringUtils.generateNumericString(6), System.currentTimeMillis());
        authInfos.put(userId, info);
        return info.getPassword();
    }

    public UserAuthInfo get(UUID userId){
        return authInfos.get(userId);
    }

    public void evict(UUID userId){
        authInfos.remove(userId);
    }
}
