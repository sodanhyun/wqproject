package com.codehows.wqproject.domain.auth.service;

import com.codehows.wqproject.entity.RefreshToken;

public interface RefreshTokenService {

    public RefreshToken findByUserId(String userId);
    public RefreshToken updateRefreshToken(String userId, String newRefreshToken);
    public void invalidByUserId(String userId);

}
