package com.codehows.wqproject.repository.querydsl;

import com.codehows.wqproject.domain.question.requestDto.QuestionDto;
import com.codehows.wqproject.domain.question.responseDto.QuestionInfoRes;
import com.codehows.wqproject.entity.Lecture;

import java.util.List;

public interface QuestionRepositoryCustom {

    String findMaxKey(Lecture lecture);

    List<QuestionInfoRes> findAllByLCodeDesc(String lCode);

}
