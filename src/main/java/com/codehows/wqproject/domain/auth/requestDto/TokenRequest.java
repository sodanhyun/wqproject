package com.codehows.wqproject.domain.auth.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRequest {
    private String refreshToken;
}
