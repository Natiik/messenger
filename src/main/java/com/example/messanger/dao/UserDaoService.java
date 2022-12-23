package com.example.messanger.dao;

import com.example.messanger.dao.entity.UserEntity;
import com.example.messanger.dao.entity.UserInfoEntity;
import com.example.messanger.dao.repository.UserInfoRepository;
import com.example.messanger.dao.repository.UserRepository;
import com.example.messanger.object.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDaoService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public UserInfo findUserInfoById(UUID id) {
        Optional<UserInfoEntity> entityOp = userInfoRepository.findById(id);
        if (entityOp.isEmpty()) {
            return UserInfo.builder().build(); // todo think which way is better
        }
        return entityOp.get().toData();
    }

    public Optional<UserInfo> findUserByPhoneNumber(String phoneNumber){
        Optional<UserInfoEntity> userInfo = userInfoRepository.findByPhoneNumber(phoneNumber);
        return userInfo.map(UserInfoEntity::toData);
    }

    public UserInfo saveUserInfo(UserInfo info) {
        UserInfoEntity saved = userInfoRepository.save(info.toEntity());
        return saved.toData();
    }

}
