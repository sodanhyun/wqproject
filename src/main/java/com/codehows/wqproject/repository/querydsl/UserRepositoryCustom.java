package com.codehows.wqproject.repository.querydsl;

import com.codehows.wqproject.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> getUsersNotUserRole();
}
