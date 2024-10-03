package com.codehows.wqproject.repository;

import com.codehows.wqproject.dto.LectureListDto;
import com.codehows.wqproject.entity.Lecture;
import com.codehows.wqproject.repository.querydsl.LectureRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository extends CrudRepository<Lecture,String>, LectureRepositoryCustom {

}
