package com.example.messanger.dao.entity;

import com.example.messanger.object.UserInfo;
import com.example.messanger.types.Subscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "user_accounts", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "nick_name"}))
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserInfoEntity {
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
    @Enumerated(value = EnumType.STRING)
    private Subscription subscription;

    public UserInfo toData() {
        return UserInfo.builder()
                .id(this.id)
                .firstName(this.firstName)
                .secondName(this.secondName)
                .nickName(this.nickName)
                .phoneNumber(this.phoneNumber)
                .subscription(this.subscription)
                .build();
    }
}
