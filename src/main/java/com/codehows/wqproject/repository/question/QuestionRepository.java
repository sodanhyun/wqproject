package com.codehows.wqproject.repository.question;

import com.codehows.wqproject.entity.Lecture;
import com.codehows.wqproject.entity.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question,String>, QuestionRepositoryCustom {

    String findMaxKey(Lecture lecture);

    List<Question> findAllByLecture(Lecture lecture);

    List<Question> findAllByLectureAndPick(Lecture lecture, Boolean flag);

    List<Question> findAllByMemberId(String memberId);

}
