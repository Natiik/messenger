package com.example.messanger.service;

import com.example.messanger.dao.UserDaoService;
import com.example.messanger.object.User;
import com.example.messanger.object.UserInfo;
import com.example.messanger.types.Subscription;
import com.example.messanger.util.NumericStringUtils;
import com.example.messanger.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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
                .firstName("user" + NumericStringUtils.generateNumericString(8))
                .build();
        return userDaoService.saveUserInfo(newUserInfo);
    }

    public Optional<UserInfo> findUserInfoByPhoneNumber(String phoneNumber) {
        Validator.validatePhoneNumber(phoneNumber);
        return userDaoService.findUserByPhoneNumber(phoneNumber);
    }

    public Optional<UserInfo> findUserInfoById(UUID userId) {
        return userDaoService.findUserInfoById(userId);
    }

    public List<UserInfo> findUsersInfoByName(String firstName, String secondName) {
        if (firstName == null) {
            log.error("Trying to search for user with null name");
            throw new IllegalArgumentException("The search name can't be null");
        }
        return userDaoService.findUsersInfoByName(firstName, secondName);
    }

    public Optional<UserInfo> findUserInfoByNickName(String nickName) {
        if (nickName == null) {
            log.error("Nickname of the user can't be null");
            throw new IllegalArgumentException("Nickname can't ba null");
        }
        return userDaoService.findUserInfoByNickName(nickName);
    }

    public List<UserInfo> findUsersInfoByNickNameContaining(String partNickName) {
        if (partNickName == null) {
            return Collections.emptyList();// todo search for all with pagination
        }
        return userDaoService.findUsersInfoByNickNameContaining(partNickName);
    }

    public UserInfo saveUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            log.error("Can't save user info since it is null");
            throw new IllegalArgumentException("UserInfo is null");
        }
        return userDaoService.saveUserInfo(userInfo);
    }

    public User saveUser(User user) {
        if (user == null) {
            log.error("Can't save user since it is null");
            throw new IllegalArgumentException("User is null");
        }

        return userDaoService.saveUser(user);
    }
}
