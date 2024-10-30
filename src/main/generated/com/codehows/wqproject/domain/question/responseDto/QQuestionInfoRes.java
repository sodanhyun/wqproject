package com.codehows.wqproject.domain.question.responseDto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.codehows.wqproject.domain.question.responseDto.QQuestionInfoRes is a Querydsl Projection type for QuestionInfoRes
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QQuestionInfoRes extends ConstructorExpression<QuestionInfoRes> {

    private static final long serialVersionUID = -1993006402L;

    public QQuestionInfoRes(com.querydsl.core.types.Expression<String> qCode, com.querydsl.core.types.Expression<String> lCode, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<String> userId, com.querydsl.core.types.Expression<String> userName, com.querydsl.core.types.Expression<Integer> answerCnt, com.querydsl.core.types.Expression<Integer> likesCnt, com.querydsl.core.types.Expression<Boolean> isPicked, com.querydsl.core.types.Expression<java.time.LocalDateTime> regTime, com.querydsl.core.types.Expression<java.time.LocalDateTime> updateTime) {
        super(QuestionInfoRes.class, new Class<?>[]{String.class, String.class, String.class, String.class, String.class, int.class, int.class, boolean.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, qCode, lCode, content, userId, userName, answerCnt, likesCnt, isPicked, regTime, updateTime);
    }

}

