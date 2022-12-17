package com.example.messanger.dao.entity;

import com.example.messanger.object.User;
import com.example.messanger.types.Subscription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;


@Entity
@Table(name = "user_accounts", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "nick_name"}))
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Slf4j
public class UserEntity {
    @Id
    @Column(name = "id")
    @GenericGenerator(name = "uuidGenerator", strategy = "com.example.messanger.dao.generator.UuidGenerator")
    @GeneratedValue(generator = "uuidGenerator")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "subscription")
    private Subscription subscription;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_type")
    private String imageType;

    @Column(name = "description")
    private String description;

    @Column(name = "add_info")
    private String additionalInfo;

    public User toData() {
        try {
            JsonNode addInfo = new ObjectMapper().readTree(this.additionalInfo);
            User user = User.builder()
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
            return user;
        } catch (JsonProcessingException e) {
            log.error("Failed to cast user entity to user", e);
            throw new RuntimeException(e);
        }
    }
}
