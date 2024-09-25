package com.codehows.wqproject.repository;

import com.codehows.wqproject.entity.RefreshToken;
import com.codehows.wqproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    void deleteByUser(User user);
}
