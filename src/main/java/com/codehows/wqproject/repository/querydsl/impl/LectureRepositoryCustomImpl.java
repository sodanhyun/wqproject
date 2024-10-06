package com.codehows.wqproject.repository.querydsl.impl;

import com.codehows.wqproject.domain.lecture.responseDto.LectureRes;
import com.codehows.wqproject.repository.querydsl.LectureRepositoryCustom;
import com.codehows.wqproject.repository.querydsl.Querydsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<LectureRes> findAllList() {
        return select(Projections.fields(LectureRes.class,
                    lecture.lCode,
                    lecture.title,
                    lecture.sdate,
                    lecture.edate,
                    lecture.active))
                .from(lecture)
                .where(lecture.edate.goe(LocalDateTime.now().minusMonths(1)))
                .orderBy(lecture.sdate.desc())
                .fetch();
    }

    @Override
    public List<LectureRes> searchList(String keyword, LocalDateTime sdate, LocalDateTime edate) {
        LocalDateTime startDate = sdate == null ? LocalDateTime.MIN : sdate;
        LocalDateTime endDate = edate == null ? LocalDateTime.now() : edate;

        if(keyword == null || keyword.isEmpty()) {
            if(sdate == null && edate == null) {
                return select(Projections.fields(LectureRes.class,
                            lecture.lCode,
                            lecture.title,
                            lecture.sdate,
                            lecture.edate,
                            lecture.active))
                        .from(lecture)
                        .orderBy(lecture.edate.desc())
                        .fetch();
            }
            return select(Projections.fields(LectureRes.class,
                        lecture.lCode,
                        lecture.title,
                        lecture.sdate,
                        lecture.edate,
                        lecture.active))
                    .from(lecture)
                    .where(searchDateFilter(startDate, endDate))
                    .orderBy(lecture.edate.desc())
                    .fetch();
        }else {
            if(sdate == null && edate == null) {
                return select(Projections.fields(LectureRes.class,
                            lecture.lCode,
                            lecture.title,
                            lecture.sdate,
                            lecture.edate,
                            lecture.active))
                        .from(lecture)
                        .where(searchKeywordFilter(keyword))
                        .orderBy(lecture.edate.desc())
                        .fetch();
            }
            return select(Projections.fields(LectureRes.class,
                        lecture.lCode,
                        lecture.title,
                        lecture.sdate,
                        lecture.edate,
                        lecture.active))
                    .from(lecture)
                    .where(searchKeywordFilter(keyword)
                            .and(searchDateFilter(startDate, endDate)))
                    .orderBy(lecture.edate.desc())
                    .fetch();
        }


    }
}
