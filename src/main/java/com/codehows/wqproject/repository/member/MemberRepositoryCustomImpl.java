package com.codehows.wqproject.repository.member;

import com.codehows.wqproject.constant.Role;
import com.codehows.wqproject.entity.Member;
import com.codehows.wqproject.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    private final QMember qMember = QMember.member;

    @Override
    public List<Member> authorityEdit() {
        return queryFactory
                .select(qMember)
                .from(qMember)
                .where(qMember.role.ne(Role.USER))
                .orderBy(qMember.regTime.desc())
                .fetch();
    }
}
