package com.codehows.wqproject.repository.querydsl.impl;

import com.codehows.wqproject.domain.lecture.responseDto.LectureRes;
import com.codehows.wqproject.repository.querydsl.LectureRepositoryCustom;
import com.codehows.wqproject.repository.querydsl.Querydsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.codehows.wqproject.entity.QLecture.lecture;

public class LectureRepositoryCustomImpl extends Querydsl4RepositorySupport implements LectureRepositoryCustom {

    protected LectureRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    private BooleanExpression searchDateFilter(LocalDateTime sdate, LocalDateTime edate) {
        BooleanExpression endDateIsGoeStartDate = lecture.edate.goe(sdate);
        BooleanExpression startDateIsLoeEndDate = lecture.sdate.loe(edate);

        return Expressions.allOf(endDateIsGoeStartDate, startDateIsLoeEndDate);
    }

    private BooleanExpression searchKeywordFilter(String keyword) {
        BooleanExpression containTitle = lecture.title.contains(keyword);
        BooleanExpression containSpeaker = lecture.speaker.contains(keyword);

        return Expressions.anyOf(containTitle, containSpeaker);
    }

    @Override
    public String findMaxKey() {
        return select(lecture.lCode.max())
                .from(lecture)
                .fetchOne();
    }

    @Override
    public Page<LectureRes> findAllList(Pageable pageable) {
        List<LectureRes> content =
                select(Projections.fields(LectureRes.class,
                    lecture.lCode,
                    lecture.title,
                    lecture.sdate,
                    lecture.edate,
                    lecture.active))
                .from(lecture)
                .where(lecture.edate.goe(LocalDateTime.now().minusMonths(1)))
                .orderBy(lecture.sdate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Optional<Long> total = Optional.ofNullable(
                select(Wildcard.count)
                        .from(lecture)
                        .fetchOne()
        );
        return new PageImpl<>(content, pageable, total.orElse(0L));
    }

    @Override
    public Page<LectureRes> searchList(String keyword, LocalDateTime sdate, LocalDateTime edate, Pageable pageable) {
        LocalDateTime startDate = sdate == null ? LocalDateTime.MIN : sdate;
        LocalDateTime endDate = edate == null ? LocalDateTime.now() : edate;
        JPAQueryBase<LectureRes, JPAQuery<LectureRes>> query =
                select(Projections.fields(LectureRes.class,
                    lecture.lCode,
                    lecture.title,
                    lecture.sdate,
                    lecture.edate,
                    lecture.active))
                .from(lecture);
        if(keyword == null || keyword.isEmpty()) {
            if(sdate != null || edate != null) {
                query = query
                        .where(searchDateFilter(startDate, endDate));
            }
        }else {
            if(sdate != null || edate != null) {
                query = query
                        .where(searchKeywordFilter(keyword).and(searchDateFilter(startDate, endDate)));
            }else {
                query = query
                        .where(searchKeywordFilter(keyword));
            }
        }
        List<LectureRes> content =  query
                .orderBy(lecture.edate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Optional<Long> total = Optional.ofNullable(
                select(Wildcard.count)
                        .from(lecture)
                        .fetchOne()
        );
        return new PageImpl<>(content, pageable, total.orElse(0L));
    }
}
