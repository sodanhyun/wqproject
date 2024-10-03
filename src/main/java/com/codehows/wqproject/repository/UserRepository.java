package com.codehows.wqproject.repository;

import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.repository.querydsl.UserRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String>, UserRepositoryCustom {

    List<User> findAll();

}
