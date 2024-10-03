package com.codehows.wqproject.domain.answer.controller;

import com.codehows.wqproject.domain.answer.service.impl.AnswerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
@Slf4j
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/list/{qCode}")
    public ResponseEntity<?> questionList(@PathVariable String qCode) {
        try {
            return ResponseEntity.ok().body(answerService.findByQuestion(qCode));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 질문을 찾을 수 없습니다.", HttpStatus.METHOD_NOT_ALLOWED);
        }
    }
}
