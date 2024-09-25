package com.codehows.wqproject.auth.jwt;

import com.codehows.wqproject.entity.RefreshToken;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

    public RefreshToken findByUser(User user) {
        return refreshTokenRepository.findByUser(user).orElse(null);
    }

    public String createNewToken(User user) {
        String newRefreshToken = tokenProvider.createRefreshToken(Duration.ofDays(1));
        RefreshToken existRefreshToken = findByUser(user);
        if(existRefreshToken == null) {
            refreshTokenRepository.save(new RefreshToken(user, newRefreshToken));
        }else {
            existRefreshToken.update(newRefreshToken);
        }
        return newRefreshToken;
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public void removeToken(String refreshToken) {
        RefreshToken findToken = refreshTokenRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow(EntityNotFoundException::new);
        refreshTokenRepository.delete(findToken);
    }
}
