package com.goormthon.halmang.domain.auth.responseDto;

import com.goormthon.halmang.constant.enumVal.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenRes {
    private String userId;
    private String accessToken;
    private String refreshToken;
    private String userType;
    private UserRole userRole;
}