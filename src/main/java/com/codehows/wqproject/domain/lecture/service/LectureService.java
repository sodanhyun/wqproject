package com.codehows.wqproject.domain.lecture.service;

import com.codehows.wqproject.domain.lecture.requestDto.LectureReq;
import com.codehows.wqproject.domain.lecture.requestDto.LectureSearchConditionReq;
import com.codehows.wqproject.domain.lecture.responseDto.LectureInfoRes;
import com.codehows.wqproject.domain.lecture.responseDto.LectureLimitRes;
import com.codehows.wqproject.domain.lecture.responseDto.LectureDetailRes;
import com.codehows.wqproject.entity.Lecture;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureService {
    public List<LectureInfoRes> getAllActivatedList();
    public List<LectureInfoRes> getFilteredAllList(LocalDateTime date);
    public Page<LectureInfoRes> getFilteredListByPaging(LectureSearchConditionReq dto, Pageable pageable);
    public LectureDetailRes findOne(String lCode);
    public LectureLimitRes getLimitInfo(String lCode);
    public void regist (LectureReq lectureReq, MultipartFile img);
    public void deleteLectureImage(Lecture lecture);
    public void delete(String lCode);
    public void update(String lCode, LectureReq lectureReq, MultipartFile img);
    public String updateActive(String lCode, Boolean flag);
    public Resource lectureImage(String lCode, String thumbs) throws Exception;
}
