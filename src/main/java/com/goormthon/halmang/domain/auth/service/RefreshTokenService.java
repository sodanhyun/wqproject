package com.goormthon.halmang.domain.auth.service;


import com.goormthon.halmang.entity.RefreshToken;
import com.goormthon.halmang.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    public Optional<RefreshToken> findByUser(User user);
    public RefreshToken findByUserId(String userId);
    public RefreshToken updateRefreshToken(String userId, String newRefreshToken);
    public void invalidByUserId(String userId);
    public void updateOrSaveByUser(User user, String refreshToken);

}
