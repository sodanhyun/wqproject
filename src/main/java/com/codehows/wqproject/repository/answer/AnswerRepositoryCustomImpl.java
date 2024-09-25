package com.codehows.wqproject.repository.answer;

import com.codehows.wqproject.domain.answer.requestDto.AnswerDto;
import com.codehows.wqproject.entity.QAnswer;
import com.codehows.wqproject.entity.Question;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AnswerRepositoryCustomImpl implements AnswerRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QAnswer qAnswer = QAnswer.answer;

    @Override
    public String findMaxKey(Question question) {
        return queryFactory
                .select(qAnswer.aCode.max())
                .from(qAnswer)
                .where(qAnswer.question.eq(question))
                .fetchOne();
    }

    @Override
    public List<AnswerDto> findByQCode(String qCode) {
        return queryFactory
                .select(Projections.fields(AnswerDto.class,
                        qAnswer.aCode,
                        qAnswer.question.qCode,
                        qAnswer.content))
                .from(qAnswer)
                .where(qAnswer.question.qCode.eq(qCode))
                .fetch();
    }
}
