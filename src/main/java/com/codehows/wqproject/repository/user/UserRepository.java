package com.codehows.wqproject.repository.user;

import com.codehows.wqproject.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String>, UserRepositoryCustom {

    List<User> findAll();

    List<User> authorityEdit();

}
