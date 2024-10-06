package com.codehows.wqproject.repository;

import com.codehows.wqproject.entity.Lecture;
import com.codehows.wqproject.repository.querydsl.LectureRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

public interface LectureRepository extends CrudRepository<Lecture,String>, LectureRepositoryCustom {

}
