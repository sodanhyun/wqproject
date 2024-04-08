package com.codehows.wqproject.entity;

import com.codehows.wqproject.constant.Role;
import com.codehows.wqproject.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter @Setter @ToString
@NoArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @Column(name = "member_id")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    Member(String id, String name, String password, Role role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public static Member createMember(MemberDto memberDto) {
        return Member.builder()
                .id(memberDto.getId())
                .name(memberDto.getName())
                .password(memberDto.getPassword())
                .role(Role.USER)
                .build();
    }

    public Member updateMember(String name) {
        this.name = name;
        return this;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

}
