package com.codehows.wqproject.auth.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    TEMP("ROLE_TEMP", "임시 사용자"), USER("ROLE_USER", "일반 사용자"), ADMIN("ROLE_ADMIN", "관리자");

    private final String key;

    private final String title;
}