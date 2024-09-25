package com.codehows.wqproject.domain.lecture.controller;

import com.codehows.wqproject.domain.lecture.requestDto.LectureDto;
import com.codehows.wqproject.domain.lecture.requestDto.LectureListConditionDto;
import com.codehows.wqproject.domain.lecture.responseDto.LectureListDto;
import com.codehows.wqproject.domain.lecture.service.impl.LectureServiceImpl;
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

    private final LectureServiceImpl lectureServiceImpl;

    @GetMapping("/list")
    public ResponseEntity<List<LectureListDto>> lectureList() {
        return ResponseEntity.ok().body(lectureServiceImpl.allList());
    }

    @PostMapping("/filteredList")
    public ResponseEntity<List<LectureListDto>> lectureListUpload(LectureListConditionDto dto) {
        return ResponseEntity.ok().body(lectureServiceImpl.findFilteredList(dto));
    }

    @PostMapping(value = "/regist")
    public ResponseEntity<?> regLecture(@RequestPart(value = "post") LectureDto lectureDto,
                                        @RequestPart(value = "image", required = false) MultipartFile img) {
        lectureServiceImpl.regist(lectureDto, img);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> lectureDetail(@PathVariable String code) {
        try {
            return ResponseEntity.ok().body(lectureServiceImpl.findLecture(code));
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 강의를 찾을 수 없습니다.", HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> lectureDelete(@PathVariable String code) {
        lectureServiceImpl.delete(code);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{code}")
    public ResponseEntity<LectureDto> lectureUpdate(@PathVariable String code,
                                                    @RequestPart(value = "post") LectureDto lectureDto,
                                                    @RequestPart(value = "image", required = false) MultipartFile img) {
        lectureServiceImpl.update(code, lectureDto, img);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/active")
    public ResponseEntity<String> lectureActive(LectureDto lectureDto) {
        return ResponseEntity.ok().body(lectureServiceImpl.setOrInit(lectureDto.getLCode(), lectureDto.getActive()).toString());
    }
    
}
