package com.codehows.wqproject.repository.lecture;

import com.codehows.wqproject.dto.LectureListDto;
import com.codehows.wqproject.dto.QLectureListDto;
import com.codehows.wqproject.entity.QLecture;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class LectureRepositoryCustomImpl implements LectureRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QLecture qLecture = QLecture.lecture;

    private BooleanExpression searchDateFilter(LocalDateTime sdate, LocalDateTime edate) {
        BooleanExpression endDateIsGoeStartDate = qLecture.edate.goe(sdate);
        BooleanExpression startDateIsLoeEndDate = qLecture.sdate.loe(edate);

        return Expressions.allOf(endDateIsGoeStartDate, startDateIsLoeEndDate);
    }

    private BooleanExpression searchKeywordFilter(String keyword) {
        BooleanExpression containTitle = qLecture.title.contains(keyword);
        BooleanExpression containSpeaker = qLecture.speaker.contains(keyword);

        return Expressions.anyOf(containTitle, containSpeaker);
    }

    @Override
    public String findMaxKey() {
        return queryFactory
                .select(qLecture.lCode.max())
                .from(qLecture)
                .fetchOne();
    }

    @Override
    public List<LectureListDto> findAllList() {
        return queryFactory
                .select(new QLectureListDto(qLecture.lCode, qLecture.title, qLecture.sdate, qLecture.edate, qLecture.active))
                .from(qLecture)
                .where(qLecture.edate.goe(LocalDateTime.now().minusMonths(1)))
                .orderBy(qLecture.sdate.desc())
                .fetch();
    }

    @Override
    public List<LectureListDto> searchList(String keyword, LocalDateTime sdate, LocalDateTime edate) {
        LocalDateTime startDate = sdate == null ? LocalDateTime.MIN : sdate;
        LocalDateTime endDate = edate == null ? LocalDateTime.now() : edate;

        if(keyword == null || keyword.isEmpty()) {
            if(sdate == null && edate == null) {
                return queryFactory
                        .select(new QLectureListDto(qLecture.lCode, qLecture.title, qLecture.sdate, qLecture.edate, qLecture.active))
                        .from(qLecture)
                        .orderBy(qLecture.edate.desc())
                        .fetch();
            }
            return queryFactory
                    .select(new QLectureListDto(qLecture.lCode, qLecture.title, qLecture.sdate, qLecture.edate, qLecture.active))
                    .from(qLecture)
                    .where(searchDateFilter(startDate, endDate))
                    .orderBy(qLecture.edate.desc())
                    .fetch();
        }else {
            if(sdate == null && edate == null) {
                return queryFactory
                        .select(new QLectureListDto(qLecture.lCode, qLecture.title, qLecture.sdate, qLecture.edate, qLecture.active))
                        .from(qLecture)
                        .where(searchKeywordFilter(keyword))
                        .orderBy(qLecture.edate.desc())
                        .fetch();
            }
            return queryFactory
                    .select(new QLectureListDto(qLecture.lCode, qLecture.title, qLecture.sdate, qLecture.edate, qLecture.active))
                    .from(qLecture)
                    .where(searchKeywordFilter(keyword)
                            .and(searchDateFilter(startDate, endDate)))
                    .orderBy(qLecture.edate.desc())
                    .fetch();
        }


    }
}
