package com.codehows.wqproject.repository.querydsl.impl;

import com.codehows.wqproject.constant.enumVal.UserRole;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.repository.querydsl.Querydsl4RepositorySupport;
import com.codehows.wqproject.repository.querydsl.UserRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.codehows.wqproject.entity.QUser.user;

public class UserRepositoryCustomImpl extends Querydsl4RepositorySupport implements UserRepositoryCustom  {

    protected UserRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    @Override
    public List<User> getAllUsers() {
        return select(user)
                .from(user)
                .orderBy(user.regTime.desc())
                .fetch();
    }
}
