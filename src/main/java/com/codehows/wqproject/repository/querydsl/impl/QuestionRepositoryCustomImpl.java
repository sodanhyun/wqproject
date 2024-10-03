package com.codehows.wqproject.repository.querydsl.impl;

import com.codehows.wqproject.entity.Lecture;
import com.codehows.wqproject.entity.QQuestion;
import com.codehows.wqproject.repository.querydsl.Querydsl4RepositorySupport;
import com.codehows.wqproject.repository.querydsl.QuestionRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

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

}
