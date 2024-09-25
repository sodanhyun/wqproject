package com.codehows.wqproject.repository.lecture;

import com.codehows.wqproject.domain.lecture.responseDto.LectureListDto;
import com.codehows.wqproject.entity.Lecture;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository extends CrudRepository<Lecture,String>, LectureRepositoryCustom {

    String findMaxKey();

    List<LectureListDto> findAllList();

    List<LectureListDto> searchList(String keyword, LocalDateTime sdate, LocalDateTime edate);

}
