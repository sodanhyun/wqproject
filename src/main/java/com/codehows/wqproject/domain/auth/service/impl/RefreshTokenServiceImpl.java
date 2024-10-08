package com.codehows.wqproject.domain.auth.service.impl;

import com.codehows.wqproject.auth.jwt.JwtTokenProvider;
import com.codehows.wqproject.domain.auth.service.RefreshTokenService;
import com.codehows.wqproject.entity.RefreshToken;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByUserId(String userId) {
        return refreshTokenRepository.findByUserId(userId).orElse(null);
    }

    public RefreshToken updateRefreshToken(String userId, String newRefreshToken) {
        return findByUserId(userId).update(newRefreshToken);
    }

}
