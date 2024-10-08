package com.codehows.wqproject.domain.auth.service;

import com.codehows.wqproject.domain.auth.requestDto.LoginDto;
import com.codehows.wqproject.domain.auth.responseDto.TokenRes;
import org.springframework.security.core.Authentication;

public interface AuthService {

    public TokenRes login(LoginDto loginDto);

    public void invalidRefreshToken(Authentication authentication);
}
