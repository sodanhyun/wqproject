package com.codehows.wqproject.repository.querydsl;

import com.codehows.wqproject.domain.lecture.responseDto.LectureDetailRes;
import com.codehows.wqproject.domain.lecture.responseDto.LectureInfoRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepositoryCustom {

    String findMaxKey();
    List<LectureInfoRes> allListByMonth(LocalDateTime date);
    Page<LectureInfoRes> searchList(String keyword, LocalDateTime sdate, LocalDateTime edate, Pageable pageable);
}
