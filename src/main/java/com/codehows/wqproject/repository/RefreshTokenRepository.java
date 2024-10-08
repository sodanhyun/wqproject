package com.codehows.wqproject.repository;

import com.codehows.wqproject.entity.RefreshToken;
import com.codehows.wqproject.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByUserId(String userId);

    Optional<RefreshToken> findByValue(String value);

    void deleteByUserId(String userId);

    void deleteByUser(User user);
}
