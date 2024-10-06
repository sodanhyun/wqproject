package com.codehows.wqproject.domain.lecture.controller;

import com.codehows.wqproject.dto.LectureDto;
import com.codehows.wqproject.dto.LectureListConditionDto;
import com.codehows.wqproject.dto.LectureListDto;
import com.codehows.wqproject.domain.lecture.service.impl.LectureService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
@Slf4j
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/list")
    public ResponseEntity<List<LectureListDto>> lectureList() {
        return ResponseEntity.ok().body(lectureService.allList());
    }

    @PostMapping("/filteredList")
    public ResponseEntity<List<LectureListDto>> lectureListUpload(LectureListConditionDto dto) {
        return ResponseEntity.ok().body(lectureService.findFilteredList(dto));
    }

    @PostMapping(value = "/regist")
    public ResponseEntity<?> regLecture(@RequestPart(value = "data") LectureDto lectureDto,
                                        @RequestPart(value = "image", required = false) MultipartFile img) {
        lectureService.regist(lectureDto, img);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> lectureDetail(@PathVariable String code) {
        try {
            return ResponseEntity.ok().body(lectureService.findLecture(code));
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 강의를 찾을 수 없습니다.", HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> lectureDelete(@PathVariable String code) {
        lectureService.delete(code);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{code}")
    public ResponseEntity<LectureDto> lectureUpdate(@PathVariable String code,
                                                    @RequestPart(value = "data") LectureDto lectureDto,
                                                    @RequestPart(value = "image", required = false) MultipartFile img) {
        lectureService.update(code, lectureDto, img);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/active")
    public ResponseEntity<String> lectureActive(LectureDto lectureDto) {
        return ResponseEntity.ok().body(lectureService.setOrInit(lectureDto.getLCode(), lectureDto.getActive()).toString());
    }

    @GetMapping("/limit/{lCode}")
    public ResponseEntity<?> lectureLimit(@PathVariable String lCode) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            LectureDto lectureDto = lectureService.findLecture(lCode);
            result.put("title", lectureDto.getTitle());
            result.put("limitMin", lectureDto.getLimitMin());
            result.put("active", lectureDto.getActive());
            return ResponseEntity.ok().body(result);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 강의를 찾을 수 없습니다.", HttpStatus.METHOD_NOT_ALLOWED);
        }
    }
    
}
