package com.goormthon.halmang.domain.auth.service.impl;

import com.goormthon.halmang.domain.auth.service.RefreshTokenService;
import com.goormthon.halmang.entity.RefreshToken;
import com.goormthon.halmang.entity.User;
import com.goormthon.halmang.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByUser(User user) {
        return refreshTokenRepository.findByUser(user);
    }

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

    public void updateOrSaveByUser(User user, String refreshToken) {
        RefreshToken refreshToken1 = refreshTokenRepository.findByUser(user)
                .orElse(RefreshToken.builder()
                .user(user)
                .value(refreshToken)
                .build()
        );
        refreshToken1.update(refreshToken);
        refreshTokenRepository.save(refreshToken1);
    }

}
