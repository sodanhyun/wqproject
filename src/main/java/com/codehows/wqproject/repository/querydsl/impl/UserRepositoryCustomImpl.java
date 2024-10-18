package com.codehows.wqproject.repository.querydsl.impl;

import com.codehows.wqproject.constant.enumVal.UserRole;
import com.codehows.wqproject.domain.account.responseDto.AccountInfoRes;
import com.codehows.wqproject.domain.account.responseDto.AccountPageRes;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.repository.querydsl.Querydsl4RepositorySupport;
import com.codehows.wqproject.repository.querydsl.UserRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.codehows.wqproject.entity.QLecture.lecture;
import static com.codehows.wqproject.entity.QUser.user;

public class UserRepositoryCustomImpl extends Querydsl4RepositorySupport implements UserRepositoryCustom  {

    protected UserRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    @Override
    public Page<AccountInfoRes> getAllUsersByPaging(Pageable pageable) {
        List<AccountInfoRes> content =
                select(Projections.fields(AccountInfoRes.class,
                        user.id,
                        user.name,
                        user.userRole))
                .from(user)
                .orderBy(user.regTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Optional<Long> total = Optional.ofNullable(
                select(Wildcard.count)
                        .from(user)
                        .fetchOne());
        return new PageImpl<>(content, pageable, total.orElse(0L));
    }
}
