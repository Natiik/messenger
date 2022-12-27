package com.example.messanger.dao.repository;

import com.example.messanger.dao.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInfoRepository extends JpaRepositoryImplementation<UserInfoEntity, UUID> {
    Optional<UserInfoEntity> findByPhoneNumber(String phoneNumber);


    List<UserInfoEntity> findUserInfoEntitiesByFirstNameAndSecondName(String firstName, String secondName);

    Optional<UserInfoEntity> findUserInfoEntityByNickName(String nickName);

    List<UserInfoEntity> findUserInfoEntitiesByNickNameContaining(String partNickName);
}
