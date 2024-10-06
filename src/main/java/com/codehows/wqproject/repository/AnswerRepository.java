package com.codehows.wqproject.repository;

import com.codehows.wqproject.entity.Answer;
import com.codehows.wqproject.entity.Question;
import com.codehows.wqproject.repository.querydsl.AnswerRepositoryCustom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer,String>, AnswerRepositoryCustom {

    @Modifying
    @Query("delete from Answer a where a.aCode in :ids")
    void deleteAllByIds(@Param("ids") List<Answer> ids);

    List<Answer> findAllByQuestion(Question question);

    Long countByQuestion(Question question);
}
