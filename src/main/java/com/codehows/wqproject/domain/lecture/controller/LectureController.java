package com.codehows.wqproject.domain.lecture.controller;

import com.codehows.wqproject.domain.lecture.requestDto.LectureActiveReq;
import com.codehows.wqproject.domain.lecture.requestDto.LectureReq;
import com.codehows.wqproject.domain.lecture.requestDto.LectureSearchConditionReq;
import com.codehows.wqproject.domain.lecture.responseDto.LectureLimitRes;
import com.codehows.wqproject.domain.lecture.responseDto.LectureRes;
import com.codehows.wqproject.domain.lecture.service.LectureService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
@Slf4j
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        List<LectureRes> res = lectureService.getList();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/filteredList")
    public ResponseEntity<?> filteredList(LectureSearchConditionReq req) {
        List<LectureRes> res = lectureService.getFilteredList(req);
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
            LectureRes res = lectureService.findOne(code);
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
            return ResponseEntity.ok().body(lectureService.lectureImage(code));
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 이미지를 찾을 수 없습니다.", HttpStatus.METHOD_NOT_ALLOWED);
        }
    }
    
}
