package com.codehows.wqproject.domain.answer.service.impl;

import com.codehows.wqproject.domain.answer.requestDto.AnswerDto;
import com.codehows.wqproject.domain.answer.service.AnswerService;
import com.codehows.wqproject.entity.Answer;
import com.codehows.wqproject.entity.Question;
import com.codehows.wqproject.repository.answer.AnswerRepository;
import com.codehows.wqproject.repository.question.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public AnswerDto regist(String qCode, String content) throws RuntimeException {
        Question question = questionRepository.findById(qCode)
                .orElse(null);
        if(question == null)
            throw new RuntimeException("해당 질문을 찾을 수 없습니다.");
        String maxKey = answerRepository.findMaxKey(question);
        String nextNum = maxKey == null ?"001" : (Integer.parseInt(maxKey.split("A")[1])+1) + "";
        while(nextNum.length()<3)
            nextNum = "0" + nextNum;
        String nextKey = qCode + "A" + nextNum;
        Answer answer = Answer.builder()
                .aCode(nextKey)
                .question(question)
                .content(content)
                .build();
        return AnswerDto.of(answerRepository.save(answer));
    }

    public AnswerDto update(String aCode, String content) throws RuntimeException {
        Answer answer = answerRepository.findById(aCode)
                .orElse(null);
        if(answer == null)
            throw new RuntimeException("해당 답변을 찾을 수 없습니다.");
        answer.updateContent(content);
        return AnswerDto.of(answer);
    }

    public long delete(String aCode) {
        Answer answer = answerRepository.findById(aCode).orElseGet(() -> {
            log.info("해당 질문 없음");
            throw new EntityNotFoundException();
        });
        Question question = answer.getQuestion();
        answerRepository.deleteById(aCode);
        return answerRepository.countByQuestion(question);
    }

    public List<AnswerDto> findByQuestion(String qCode) {
        Question question = questionRepository.findById(qCode)
                .orElseGet(() -> {
                    log.info("해당 질문 없음");
                    throw new EntityNotFoundException();
                });
        return answerRepository.findByQCode(qCode);
    }

}