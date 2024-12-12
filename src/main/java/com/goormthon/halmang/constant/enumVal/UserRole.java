package com.goormthon.halmang.constant.enumVal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserRole implements EntityEnumerable {
    TEMP("ROLE_TEMP", "임시 사용자"),
    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String type;

    private final String name;

    @jakarta.persistence.Converter
    public static class Converter extends EntityEnumerableConverter<UserRole> {
        public Converter() {
            super(UserRole.class);
        }
    }
}