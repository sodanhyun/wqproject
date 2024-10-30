package com.codehows.wqproject.repository;

import com.codehows.wqproject.constant.LikeId;
import com.codehows.wqproject.entity.Answer;
import com.codehows.wqproject.entity.Likes;
import com.codehows.wqproject.entity.Question;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends CrudRepository<Likes, LikeId> {

    List<Likes> findAllByQuestion(Question question);

    Optional<Likes> findByQuestionAndUserId(Question question, String userId);

    @Modifying
    @Query("delete from Likes l where l.qCode in :ids")
    void deleteAllByIds(@Param("ids") List<Answer> ids);

}
