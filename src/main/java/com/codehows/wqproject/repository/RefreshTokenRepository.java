package com.codehows.wqproject.repository;

import com.codehows.wqproject.entity.Member;
import com.codehows.wqproject.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
