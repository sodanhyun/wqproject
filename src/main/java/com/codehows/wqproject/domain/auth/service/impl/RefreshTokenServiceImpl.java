package com.codehows.wqproject.domain.auth.service.impl;

import com.codehows.wqproject.domain.auth.service.RefreshTokenService;
import com.codehows.wqproject.entity.RefreshToken;
import com.codehows.wqproject.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByUserId(String userId) {
        return refreshTokenRepository.findByUserId(userId).orElse(null);
    }

    public RefreshToken updateRefreshToken(String userId, String newRefreshToken) {
        return findByUserId(userId).update(newRefreshToken);
    }

    public void invalidByUserId(String userId) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId).orElseGet(() -> {
            log.info("리프레시 토큰이 존재하지 않는 유저입니다.");
            throw new EntityNotFoundException();
        });
        refreshToken.update("");
    }

}
