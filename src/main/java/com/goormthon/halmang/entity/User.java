package com.goormthon.halmang.entity;

import com.goormthon.halmang.auditing.BaseTimeEntity;
import com.goormthon.halmang.constant.enumVal.UserRole;
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

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    @Convert(converter =  UserRole.Converter.class)
    @Column(nullable = false)
    private UserRole userRole;

    @Builder
    public User(String id, String password, String name, UserRole userRole) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.userRole = userRole;
    }

}
