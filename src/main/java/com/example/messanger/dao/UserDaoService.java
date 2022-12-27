package com.example.messanger.dao;

import com.example.messanger.dao.entity.UserEntity;
import com.example.messanger.dao.entity.UserInfoEntity;
import com.example.messanger.dao.repository.UserInfoRepository;
import com.example.messanger.dao.repository.UserRepository;
import com.example.messanger.object.User;
import com.example.messanger.object.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDaoService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public Optional<UserInfo> findUserInfoById(UUID id) {
        Optional<UserInfoEntity> entityOp = userInfoRepository.findById(id);
        return entityOp.map(UserInfoEntity::toData);
    }

    public Optional<UserInfo> findUserByPhoneNumber(String phoneNumber) {
        Optional<UserInfoEntity> userInfo = userInfoRepository.findByPhoneNumber(phoneNumber);
        return userInfo.map(UserInfoEntity::toData);
    }

    public List<UserInfo> findUsersInfoByName(String firstName, String secondName) {
        return userInfoRepository.findUserInfoEntitiesByFirstNameAndSecondName(firstName, secondName)
                .stream()
                .map(UserInfoEntity::toData)
                .toList();
    }

    public Optional<UserInfo> findUserInfoByNickName(String nickName) {
        Optional<UserInfoEntity> optional = userInfoRepository.findUserInfoEntityByNickName(nickName);
        return optional.map(UserInfoEntity::toData);
    }

    public List<UserInfo> findUsersInfoByNickNameContaining(String partNickName) {
        return userInfoRepository.findUserInfoEntitiesByNickNameContaining(partNickName)
                .stream()
                .map(UserInfoEntity::toData)
                .toList();
    }

    public UserInfo saveUserInfo(UserInfo info) {
        UserInfoEntity saved = userInfoRepository.save(info.toEntity());
        return saved.toData();
    }

    public User saveUser(User user) {
        UserEntity saved = userRepository.save(user.toEntity());
        return saved.toData();
    }
}
