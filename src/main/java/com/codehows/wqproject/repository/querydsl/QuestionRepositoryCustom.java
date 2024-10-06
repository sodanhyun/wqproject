package com.codehows.wqproject.repository.querydsl;

import com.codehows.wqproject.entity.Lecture;

public interface QuestionRepositoryCustom {

    String findMaxKey(Lecture lecture);

}
