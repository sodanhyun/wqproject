package com.codehows.wqproject.domain.question.controller;

import com.codehows.wqproject.domain.lecture.requestDto.LectureDto;
import com.codehows.wqproject.domain.question.requestDto.QuestionDto;
import com.codehows.wqproject.domain.lecture.service.impl.LectureServiceImpl;
import com.codehows.wqproject.domain.question.service.impl.QuestionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionServiceImpl questionServiceImpl;
    private final LectureServiceImpl lectureServiceImpl;

    @GetMapping("/list/{lCode}")
    public ResponseEntity<List<QuestionDto>> questionList(@PathVariable String lCode, Authentication authentication) {
        return ResponseEntity.ok().body(questionServiceImpl.findByLecture(lCode, authentication.getName()));
    }

    @GetMapping("/picked/{lCode}")
    public ResponseEntity<List<QuestionDto>> pickedList(@PathVariable String lCode) {
        return ResponseEntity.ok().body(questionServiceImpl.findFilteredByPick(lCode));
    }

    @GetMapping("/active/{lCode}")
    public ResponseEntity<Boolean> activeCheck(@PathVariable String lCode) {
        LectureDto lectureDto = lectureServiceImpl.findLecture(lCode);
        return ResponseEntity.ok().body(lectureDto.getActive());
    }
}
