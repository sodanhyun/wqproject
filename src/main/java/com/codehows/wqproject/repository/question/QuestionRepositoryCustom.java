package com.codehows.wqproject.repository.question;

import com.codehows.wqproject.entity.Lecture;

public interface QuestionRepositoryCustom {

    String findMaxKey(Lecture lecture);

}
