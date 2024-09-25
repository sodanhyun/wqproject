package com.codehows.wqproject.domain.auth.service;

import com.codehows.wqproject.entity.User;

public interface AuthService {
    User findById(String userId);
}
