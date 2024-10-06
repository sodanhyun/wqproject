package com.codehows.wqproject.repository.querydsl;

import com.codehows.wqproject.domain.answer.requestDto.AnswerDto;
import com.codehows.wqproject.entity.Question;

import java.util.List;

public interface AnswerRepositoryCustom {
    String findMaxKey(Question question);

    List<AnswerDto> findByQCode(String qCode);

}
