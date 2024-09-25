package com.codehows.wqproject.domain.question.service.impl;

import com.codehows.wqproject.constant.LikeId;
import com.codehows.wqproject.domain.question.requestDto.QuestionDto;
import com.codehows.wqproject.domain.question.service.QuestionService;
import com.codehows.wqproject.entity.*;
import com.codehows.wqproject.repository.LikesRepository;
import com.codehows.wqproject.repository.user.UserRepository;
import com.codehows.wqproject.repository.answer.AnswerRepository;
import com.codehows.wqproject.repository.question.QuestionRepository;
import com.codehows.wqproject.repository.lecture.LectureRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final LikesRepository likesRepository;
    private final AnswerRepository answerRepository;

    public QuestionDto regist(String lCode, String content, String memberId) throws RuntimeException {
        Lecture lecture = lectureRepository.findById(lCode)
                .orElseThrow(EntityNotFoundException::new);
        if(lecture.getActive()) {
            User user = userRepository.findById(memberId)
                    .orElseThrow(EntityNotFoundException::new);
            String maxKey = questionRepository.findMaxKey(lecture);
            String nextNum = maxKey==null ? "001" : (Integer.parseInt(maxKey.split("Q")[1])+1) + "";
            while(nextNum.length()<3)
                nextNum = "0" + nextNum;
            String nextKey = lCode + "Q" + nextNum;
            Question question = Question.builder()
                    .qCode(nextKey)
                    .lecture(lecture)
                    .pick(false)
                    .member(user)
                    .content(content)
                    .build();
            return QuestionDto.of(questionRepository.save(question));
        }else {
            throw new RuntimeException("해당 강의는 비활성화 상태입니다.");
        }
    }

    public List<QuestionDto> findByLecture(String lCode, String userId) {
        Lecture lecture = lectureRepository.findById(lCode)
                .orElseThrow(EntityNotFoundException::new);
        List<QuestionDto> result = new ArrayList<>();
        for(Question q : questionRepository.findAllByLecture(lecture)) {
            Likes likes = likesRepository.findById(new LikeId(q.getQCode(), userId))
                    .orElse(null);
            QuestionDto questionDto = QuestionDto.of(q);
            if(likes!=null) {
                questionDto.setMyLike(true);
            }
            result.add(questionDto);
        }
        return result;
    }

    public List<QuestionDto> findFilteredByPick(String lCode) {
        Lecture lecture = lectureRepository.findById(lCode)
                .orElseThrow(EntityNotFoundException::new);
        return questionRepository.findAllByLectureAndPick(lecture, true)
                .stream()
                .map(QuestionDto::of)
                .collect(Collectors.toList());
    }

    public void delete(String qCode) throws RuntimeException {
        Question question = questionRepository.findById(qCode)
                .orElseThrow(EntityNotFoundException::new);
        if(!question.getPick()) {
            for(Answer a : answerRepository.findAllByQuestion(question))
                answerRepository.delete(a);
            for(Likes l : likesRepository.findAllByQuestion(question))
                likesRepository.delete(l);
            questionRepository.delete(question);
        }else {
            throw new RuntimeException("이미 채택된 질문은 삭제할 수 없습니다.");
        }
    }

    public void update(String qCode, String content) throws RuntimeException {
        log.info(qCode + " " + content);
        Question question = questionRepository.findById(qCode)
                .orElseThrow(EntityNotFoundException::new);
        if(!question.getPick()) {
            question.updateContent(content);
        }else {
            throw new RuntimeException("이미 채택된 질문은 수정할 수 없습니다.");
        }
    }

    public QuestionDto pick(String qCode) {
        Question question = questionRepository.findById(qCode)
                .orElseThrow(EntityNotFoundException::new);
        question.pickOrInit();
        return QuestionDto.of(question);
    }

    public Boolean like(String qCode, String userId) throws EntityNotFoundException {
        Question question = questionRepository.findById(qCode)
                .orElseThrow(EntityNotFoundException::new);
        Likes like = likesRepository.findByQuestionAndEmail(question, userId).orElse(null);
        if(like==null) {
            likesRepository.save(Likes.builder()
                    .qCode(qCode)
                    .question(question)
                    .email(userId)
                    .build());
            return true;
        }else {
            likesRepository.delete(like);
            return false;
        }
    }

}
