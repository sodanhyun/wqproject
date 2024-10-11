package com.codehows.wqproject.domain.question.controller;

import com.codehows.wqproject.domain.lecture.responseDto.LectureDetailRes;
import com.codehows.wqproject.domain.lecture.service.LectureService;
import com.codehows.wqproject.domain.question.requestDto.QuestionDto;
import com.codehows.wqproject.domain.question.service.impl.QuestionService;
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

    private final QuestionService questionService;
    private final LectureService lectureService;

    @GetMapping("/list/{lCode}")
    public ResponseEntity<List<QuestionDto>> questionList(@PathVariable String lCode, Authentication authentication) {
        return ResponseEntity.ok().body(questionService.findByLecture(lCode, authentication.getName()));
    }

    @GetMapping("/picked/{lCode}")
    public ResponseEntity<List<QuestionDto>> pickedList(@PathVariable String lCode) {
        return ResponseEntity.ok().body(questionService.findFilteredByPick(lCode));
    }

    @GetMapping("/active/{lCode}")
    public ResponseEntity<Boolean> activeCheck(@PathVariable String lCode) {
        LectureDetailRes res = lectureService.findOne(lCode);
        return ResponseEntity.ok().body(res.getActive());
    }
}
