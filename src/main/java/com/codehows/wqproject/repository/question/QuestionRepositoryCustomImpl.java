package com.codehows.wqproject.repository.question;

import com.codehows.wqproject.dto.QLectureListDto;
import com.codehows.wqproject.entity.Lecture;
import com.codehows.wqproject.entity.QLecture;
import com.codehows.wqproject.entity.QQuestion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QQuestion qQuestion = QQuestion.question;

    @Override
    public String findMaxKey(Lecture lecture) {
        return queryFactory
                .select(qQuestion.qCode.max())
                .from(qQuestion)
                .where(qQuestion.lecture.eq(lecture))
                .fetchOne();
    }

}
