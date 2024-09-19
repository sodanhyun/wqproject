package com.codehows.wqproject.controller;

import com.codehows.wqproject.service.LectureService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final LectureService lectureService;

    @GetMapping("/lecture/{code}")
    public ResponseEntity<?> lectureImage(@PathVariable String code) {
        try{
            return ResponseEntity.ok().body(lectureService.lectureImage(code));
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 이미지를 찾을 수 없습니다.", HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

}
