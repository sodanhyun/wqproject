package com.codehows.wqproject.repository.querydsl.impl;

import com.codehows.wqproject.domain.lecture.responseDto.LectureDetailRes;
import com.codehows.wqproject.domain.lecture.responseDto.LectureInfoRes;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.codehows.wqproject.entity.QLecture.lecture;

public class LectureRepositoryCustomImpl extends Querydsl4RepositorySupport implements LectureRepositoryCustom {

    protected LectureRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    private BooleanExpression monthScopeFilter(LocalDateTime date) {
        LocalDate localDate = date.toLocalDate();
        LocalDateTime sdate = localDate.atStartOfDay();
        LocalDateTime edate = localDate.withDayOfMonth(localDate.lengthOfMonth()).atTime(23, 59, 59);
        BooleanExpression endDateIsGoeStartDate = lecture.edate.goe(sdate);
        BooleanExpression startDateIsLoeEndDate = lecture.sdate.loe(edate);

        return Expressions.allOf(endDateIsGoeStartDate, startDateIsLoeEndDate);
    }

    private BooleanExpression searchDateFilter(LocalDateTime date) {
        BooleanExpression endDateIsGoeStartDate = lecture.edate.goe(date);
        BooleanExpression startDateIsLoeEndDate = lecture.sdate.loe(date);

        return Expressions.allOf(endDateIsGoeStartDate, startDateIsLoeEndDate);
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
    public List<LectureInfoRes> allStartedList() {
        LocalDateTime date = LocalDateTime.now();
        return select(Projections.fields(LectureInfoRes.class,
                lecture.lCode,
                lecture.title,
                lecture.sdate,
                lecture.edate,
                lecture.active))
                .from(lecture)
                .where(searchDateFilter(date))
                .fetch();
    }

    @Override
    public List<LectureInfoRes> allListByMonth(LocalDateTime date) {
        return select(Projections.fields(LectureInfoRes.class,
                lecture.lCode,
                lecture.title,
                lecture.sdate,
                lecture.edate,
                lecture.active))
                .from(lecture)
                .where(monthScopeFilter(date))
                .fetch();
    }

    @Override
    public Page<LectureInfoRes> searchList(String keyword, LocalDateTime sdate, LocalDateTime edate, Pageable pageable) {
        LocalDateTime startDate = sdate == null ? LocalDateTime.MIN : sdate;
        LocalDateTime endDate = edate == null ? LocalDateTime.now() : edate;
        JPAQueryBase<LectureInfoRes, JPAQuery<LectureInfoRes>> query =
                select(Projections.fields(LectureInfoRes.class,
                    lecture.lCode,
                    lecture.title,
                    lecture.sdate,
                    lecture.edate,
                    lecture.active))
                .from(lecture);
        JPAQueryBase<Long, JPAQuery<Long>> pagingQuery =
                select(Wildcard.count)
                .from(lecture);
        if(keyword == null || keyword.isEmpty()) {
            if(sdate != null || edate != null) {
                query = query.where(searchDateFilter(startDate, endDate));
                pagingQuery = pagingQuery.where(searchDateFilter(startDate, endDate));
            }
        }else {
            if(sdate != null || edate != null) {
                query = query.where(searchKeywordFilter(keyword).and(searchDateFilter(startDate, endDate)));
                pagingQuery = pagingQuery.where(searchKeywordFilter(keyword).and(searchDateFilter(startDate, endDate)));
            }else {
                query = query.where(searchKeywordFilter(keyword));
                pagingQuery = pagingQuery.where(searchKeywordFilter(keyword));
            }
        }
        List<LectureInfoRes> content =  query
                .orderBy(lecture.lCode.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Optional<Long> total = Optional.ofNullable(pagingQuery.fetchOne());
        return new PageImpl<>(content, pageable, total.orElse(0L));
    }
}
