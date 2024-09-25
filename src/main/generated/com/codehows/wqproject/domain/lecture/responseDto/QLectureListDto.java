package com.codehows.wqproject.domain.lecture.responseDto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.codehows.wqproject.domain.lecture.responseDto.QLectureListDto is a Querydsl Projection type for LectureListDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QLectureListDto extends ConstructorExpression<LectureListDto> {

    private static final long serialVersionUID = -283978035L;

    public QLectureListDto(com.querydsl.core.types.Expression<String> lCode, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<java.time.LocalDateTime> sdate, com.querydsl.core.types.Expression<java.time.LocalDateTime> edate, com.querydsl.core.types.Expression<Boolean> active) {
        super(LectureListDto.class, new Class<?>[]{String.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, boolean.class}, lCode, title, sdate, edate, active);
    }

}

