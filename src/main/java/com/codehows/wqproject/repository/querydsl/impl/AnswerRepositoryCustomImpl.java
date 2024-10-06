package com.codehows.wqproject.repository.querydsl.impl;

import com.codehows.wqproject.domain.answer.requestDto.AnswerDto;
import com.codehows.wqproject.entity.Question;
import com.codehows.wqproject.repository.querydsl.AnswerRepositoryCustom;
import com.codehows.wqproject.repository.querydsl.Querydsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.codehows.wqproject.entity.QAnswer.answer;

public class AnswerRepositoryCustomImpl extends Querydsl4RepositorySupport implements AnswerRepositoryCustom {

    protected AnswerRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    @Override
    public String findMaxKey(Question question) {
        return select(answer.aCode.max())
                .from(answer)
                .where(answer.question.eq(question))
                .fetchOne();
    }

    @Override
    public List<AnswerDto> findByQCode(String qCode) {
        return select(Projections.fields(AnswerDto.class,
                        answer.aCode,
                        answer.question.qCode,
                        answer.content))
                .from(answer)
                .where(answer.question.qCode.eq(qCode))
                .fetch();
    }
}
