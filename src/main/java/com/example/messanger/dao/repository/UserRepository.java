package com.example.messanger.dao.repository;


import com.example.messanger.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<UserEntity, UUID> {
}
