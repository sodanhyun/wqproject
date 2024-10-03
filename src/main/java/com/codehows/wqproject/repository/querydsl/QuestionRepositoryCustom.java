package com.codehows.wqproject.repository.querydsl;

import com.codehows.wqproject.dto.AnswerDto;
import com.codehows.wqproject.entity.Lecture;
import com.codehows.wqproject.entity.Question;

import java.util.List;

public interface QuestionRepositoryCustom {

    String findMaxKey(Lecture lecture);

}
