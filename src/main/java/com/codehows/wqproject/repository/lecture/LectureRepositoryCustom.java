package com.codehows.wqproject.repository.lecture;

import com.codehows.wqproject.domain.lecture.responseDto.LectureListDto;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepositoryCustom {

    String findMaxKey();
    List<LectureListDto> findAllList();
    List<LectureListDto> searchList(String keyword, LocalDateTime sdate, LocalDateTime edate);
}
