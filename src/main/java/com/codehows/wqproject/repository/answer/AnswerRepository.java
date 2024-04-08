package com.codehows.wqproject.repository.answer;

import com.codehows.wqproject.dto.AnswerDto;
import com.codehows.wqproject.entity.Answer;
import com.codehows.wqproject.entity.Lecture;
import com.codehows.wqproject.entity.Question;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer,String>, AnswerRepositoryCustom {
    String findMaxKey(Question question);

    List<AnswerDto> findByQCode(String qCode);
    List<Answer> findAllByQuestion(Question question);

    @Modifying
    @Query("delete from Answer a where a.aCode in :ids")
    void deleteAllByIds(@Param("ids") List<Answer> ids);

    Long countByQuestion(Question question);
}
