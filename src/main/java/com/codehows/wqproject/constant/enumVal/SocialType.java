package com.codehows.wqproject.constant.enumVal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SocialType implements EntityEnumerable {
    KAKAO("kakao", "카카오 소셜 유저"),
    GOOGLE("google", "구글 소셜 유저"),
    OWN("own", "자체 유저");

    private final String type;
    private final String name;

    @jakarta.persistence.Converter
    public static class Converter extends EntityEnumerableConverter<SocialType> {
        public Converter() {
            super(SocialType.class);
        }
    }
}
