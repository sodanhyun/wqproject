package com.codehows.wqproject.entity;

import com.codehows.wqproject.auditing.BaseTimeEntity;
import com.codehows.wqproject.auth.user.Role;
import com.codehows.wqproject.constant.SocialType;
import com.codehows.wqproject.domain.account.responseDto.UserDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @ToString
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "user_id")
    private String id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Builder
    User(String id, String email, String name, String password, Role role, SocialType socialType) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.socialType = socialType;
    }

    public static User create(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .role(Role.USER)
                .build();
    }

    public User update(String name) {
        this.name = name;
        return this;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

}
