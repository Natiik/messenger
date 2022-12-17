package com.example.messanger.object;

import com.example.messanger.dao.entity.UserEntity;
import com.example.messanger.types.Subscription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Data
@Slf4j
@Builder
@AllArgsConstructor
public class User {
    private UUID id;
    private String firstName;
    private String secondName;
    private String nickName;
    private String phoneNumber;
    private Subscription subscription;
    private byte[] image;
    private String imageType;
    private String description;
    private JsonNode additionalInfo;

    public UserEntity toEntity() {
        try {
            String addInfo = new ObjectMapper().writeValueAsString(this.additionalInfo);
            UserEntity entity = UserEntity.builder()
                    .id(this.id)
                    .firstName(this.firstName)
                    .secondName(this.secondName)
                    .nickName(this.nickName)
                    .phoneNumber(this.phoneNumber)
                    .subscription(this.subscription)
                    .image(this.image)
                    .imageType(this.imageType)
                    .description(this.description)
                    .additionalInfo(addInfo)
                    .build();
            return entity;
        } catch (JsonProcessingException e) {
            log.error("Failed to cast user to user entity, wrong additional info", e);
            throw new RuntimeException(e);
        }
    }
}
