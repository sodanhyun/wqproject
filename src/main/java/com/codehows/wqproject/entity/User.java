package com.codehows.wqproject.entity;

import com.codehows.wqproject.auditing.BaseTimeEntity;
import com.codehows.wqproject.constant.enumVal.UserRole;
import com.codehows.wqproject.constant.enumVal.SocialType;
import com.codehows.wqproject.domain.auth.requestDto.UserFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    @Convert(converter =  UserRole.Converter.class)
    @Column(nullable = false)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Convert(converter =  SocialType.Converter.class)
    @Column(nullable = false)
    private SocialType socialType;

    @Builder
    public User(String email, String password, String name, UserRole userRole, SocialType socialType) {
        this.id = email + "_" + socialType.getType();
        this.email = email;
        this.name = name;
        this.password = password;
        this.userRole = userRole;
        this.socialType = socialType;
    }



    public static User createByOwn(UserFormDto dto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        String password = passwordEncoder.encode(dto.getPassword());
        user.setPassword(password);
        user.setUserRole(UserRole.TEMP);
        user.setSocialType(SocialType.OWN);
        return user;
    }

    public User updateName(String name) {
        this.name = name;
        return this;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
