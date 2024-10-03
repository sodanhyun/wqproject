package com.codehows.wqproject.repository;

import com.codehows.wqproject.entity.Lecture;
import com.codehows.wqproject.entity.Question;
import com.codehows.wqproject.repository.querydsl.QuestionRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question,String>, QuestionRepositoryCustom {

    List<Question> findAllByLecture(Lecture lecture);

    List<Question> findAllByLectureAndPick(Lecture lecture, Boolean flag);

    List<Question> findAllByMemberId(String memberId);

}
