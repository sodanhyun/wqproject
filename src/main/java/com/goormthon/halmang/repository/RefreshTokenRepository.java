package com.goormthon.halmang.repository;

import com.goormthon.halmang.entity.RefreshToken;
import com.goormthon.halmang.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByUserId(String userId);
}
