package com.codehows.wqproject.repository.querydsl;

import com.codehows.wqproject.domain.lecture.responseDto.LectureRes;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepositoryCustom {

    String findMaxKey();
    List<LectureRes> findAllList();
    List<LectureRes> searchList(String keyword, LocalDateTime sdate, LocalDateTime edate);
}
