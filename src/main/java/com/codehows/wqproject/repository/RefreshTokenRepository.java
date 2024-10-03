package com.codehows.wqproject.repository;

import com.codehows.wqproject.entity.RefreshToken;
import com.codehows.wqproject.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByValue(String value);

    void deleteByUser(User user);
}
