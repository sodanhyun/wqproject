package com.codehows.wqproject.repository.querydsl.impl;

import com.codehows.wqproject.domain.lecture.responseDto.LectureInfoRes;
import com.codehows.wqproject.domain.question.requestDto.QuestionDto;
import com.codehows.wqproject.domain.question.responseDto.QQuestionInfoRes;
import com.codehows.wqproject.domain.question.responseDto.QuestionInfoRes;
import com.codehows.wqproject.entity.Lecture;
import com.codehows.wqproject.entity.QQuestion;
import com.codehows.wqproject.repository.querydsl.Querydsl4RepositorySupport;
import com.codehows.wqproject.repository.querydsl.QuestionRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.codehows.wqproject.entity.QQuestion.question;

public class QuestionRepositoryCustomImpl extends Querydsl4RepositorySupport implements QuestionRepositoryCustom {

    protected QuestionRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    @Override
    public String findMaxKey(Lecture lecture) {
        return select(question.qCode.max())
                .from(question)
                .where(question.lecture.eq(lecture))
                .fetchOne();
    }

    @Override
    public List<QuestionInfoRes> findAllByLCodeDesc(String lCode) {
        return select(new QQuestionInfoRes(
                question.qCode,
                question.lecture.lCode,
                question.content,
                question.user.id,
                question.user.name,
                question.answerCnt,
                question.likesCnt,
                question.isPicked,
                question.regTime,
                question.updateTime
                ))
                .from(question)
                .where(question.lecture.lCode.eq(lCode))
                .orderBy(question.qCode.desc())
                .fetch();
    }

}
