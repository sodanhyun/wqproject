package com.codehows.wqproject.repository.user;

import com.codehows.wqproject.auth.user.Role;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QMember qMember = QMember.member;

    @Override
    public List<User> authorityEdit() {
        return queryFactory
                .select(qMember)
                .from(qMember)
                .where(qMember.role.ne(Role.USER))
                .orderBy(qMember.regTime.desc())
                .fetch();
    }
}
