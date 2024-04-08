package com.codehows.wqproject.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.codehows.wqproject.dto.QQuestionDto is a Querydsl Projection type for QuestionDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QQuestionDto extends ConstructorExpression<QuestionDto> {

    private static final long serialVersionUID = -2082341334L;

    public QQuestionDto(com.querydsl.core.types.Expression<String> qCode, com.querydsl.core.types.Expression<String> lCode, com.querydsl.core.types.Expression<String> memberId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Boolean> pick, com.querydsl.core.types.Expression<Integer> likesCount, com.querydsl.core.types.Expression<java.time.LocalDateTime> regTime, com.querydsl.core.types.Expression<java.time.LocalDateTime> updateTime) {
        super(QuestionDto.class, new Class<?>[]{String.class, String.class, String.class, String.class, String.class, boolean.class, int.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, qCode, lCode, memberId, name, content, pick, likesCount, regTime, updateTime);
    }

}

