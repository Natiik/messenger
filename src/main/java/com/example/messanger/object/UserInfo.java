package com.example.messanger.object;

import com.example.messanger.dao.entity.UserEntity;
import com.example.messanger.types.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Data
@Slf4j
@Builder
@AllArgsConstructor
public class UserInfo {
    private UUID id;
    private String firstName;
    private String secondName;
    private String nickName;
    private String phoneNumber;
    private Subscription subscription;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(this.id)
                .firstName(this.firstName)
                .secondName(this.secondName)
                .nickName(this.nickName)
                .phoneNumber(this.phoneNumber)
                .subscription(this.subscription)
                .build();
    }
}
