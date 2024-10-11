package com.codehows.wqproject.domain.lecture.controller;

import com.codehows.wqproject.commonDto.PageDto;
import com.codehows.wqproject.domain.lecture.requestDto.LectureActiveReq;
import com.codehows.wqproject.domain.lecture.requestDto.LectureReq;
import com.codehows.wqproject.domain.lecture.requestDto.LectureSearchConditionReq;
import com.codehows.wqproject.domain.lecture.responseDto.LectureInfoRes;
import com.codehows.wqproject.domain.lecture.responseDto.LectureLimitRes;
import com.codehows.wqproject.domain.lecture.responseDto.LectureDetailRes;
import com.codehows.wqproject.domain.lecture.service.LectureService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.codehows.wqproject.constant.PageConstant.MAX_SIZE_PER_PAGE;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
@Slf4j
public class LectureController {

    private final LectureService lectureService;

    @GetMapping(value = {"/list"})
    public ResponseEntity<?> list(@RequestParam("date")
                                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
                                      LocalDateTime date) {

        List<LectureInfoRes> res = lectureService.getFilteredAllList(date);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value = {"/filteredList", "/filteredList/{page}"})
    public ResponseEntity<?> filteredList(LectureSearchConditionReq req,
                                          @PathVariable(required = false) Optional<Integer> page,
                                          @RequestParam(value = "itemsPerPage", required = false) Optional<Integer> itemsPerPage) {
        Pageable pageable = PageRequest.of(page.orElse(0), itemsPerPage.orElse(MAX_SIZE_PER_PAGE));
        Page<LectureInfoRes> pages = lectureService.getFilteredListByPaging(req, pageable);
        PageDto<LectureInfoRes> res = new PageDto<>();
        res.setContent(pages.getContent());
        res.setPageNumber(pages.getNumber());
        res.setTotalPages(pages.getTotalPages());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value = "/regist")
    public ResponseEntity<?> regist(@RequestPart(value = "data") LectureReq req,
                                        @RequestPart(value = "image", required = false) MultipartFile img) {
        lectureService.regist(req, img);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> detail(@PathVariable String code) {
        try {
            LectureDetailRes res = lectureService.findOne(code);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 강의를 찾을 수 없습니다.", HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> delete(@PathVariable String code) {
        lectureService.delete(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{code}")
    public ResponseEntity<?> update(@PathVariable String code,
                                           @RequestPart(value = "data") LectureReq req,
                                           @RequestPart(value = "image", required = false) MultipartFile img) {
        lectureService.update(code, req, img);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/active")
    public ResponseEntity<?> setActive(LectureActiveReq req) {
        String res = lectureService.updateActive(req.getLCode(), req.getActive());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/limit/{lCode}")
    public ResponseEntity<?> limit(@PathVariable String lCode) {
        try {
            LectureLimitRes res = lectureService.getLimitInfo(lCode);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 강의를 찾을 수 없습니다.", HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @GetMapping("/image/{code}")
    public ResponseEntity<?> lectureImage(@PathVariable String code) {
        try{
            Resource res = lectureService.lectureImage(code);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>("존재하지 않는 이미지 입니다.", HttpStatus.OK);
        }
    }
    
}
