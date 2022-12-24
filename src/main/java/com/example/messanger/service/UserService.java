package com.example.messanger.service;

import com.example.messanger.dao.UserDaoService;
import com.example.messanger.object.UserInfo;
import com.example.messanger.security.util.NumericStringUtils;
import com.example.messanger.security.util.Validator;
import com.example.messanger.types.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserDaoService userDaoService;

    public UserInfo findUserInfoOrCreateNew(String phoneNumber) {
        Optional<UserInfo> optUserInfo = findUserInfoByPhoneNumber(phoneNumber);
        if (optUserInfo.isPresent()) {
            return optUserInfo.get();
        }

        log.info("No user with phoneNumber {} was found, gonna create new", phoneNumber);
        UserInfo newUserInfo = UserInfo.builder()
                .phoneNumber(phoneNumber)
                .subscription(Subscription.ORDINARY)
                .firstName("user" + NumericStringUtils.generateNumbericString(8))
                .build();
        return userDaoService.saveUserInfo(newUserInfo);
    }

    public Optional<UserInfo> findUserInfoByPhoneNumber(String phoneNumber) {
        Validator.validatePhoneNumber(phoneNumber);
        return userDaoService.findUserByPhoneNumber(phoneNumber);
    }

    public Optional<UserInfo> findUserInfoById(UUID userId){
        return userDaoService.findUserInfoById(userId);
    }
}
