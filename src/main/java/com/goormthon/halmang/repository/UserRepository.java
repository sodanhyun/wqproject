package com.goormthon.halmang.repository;

import com.goormthon.halmang.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {

    List<User> findAll();

}
