package com.codehows.wqproject.repository.querydsl;

import com.codehows.wqproject.domain.lecture.responseDto.LectureRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepositoryCustom {

    String findMaxKey();
    Page<LectureRes> findAllList(Pageable pageable);
    Page<LectureRes> searchList(String keyword, LocalDateTime sdate, LocalDateTime edate, Pageable pageable);
}
