package com.codehows.wqproject.domain.lecture.service.impl;

import com.codehows.wqproject.domain.lecture.requestDto.LectureDto;
import com.codehows.wqproject.domain.lecture.requestDto.LectureListConditionDto;
import com.codehows.wqproject.domain.lecture.responseDto.LectureListDto;
import com.codehows.wqproject.domain.lecture.service.LectureService;
import com.codehows.wqproject.entity.*;
import com.codehows.wqproject.repository.ImageRepository;
import com.codehows.wqproject.repository.LikesRepository;
import com.codehows.wqproject.repository.answer.AnswerRepository;
import com.codehows.wqproject.repository.lecture.LectureRepository;
import com.codehows.wqproject.repository.question.QuestionRepository;
import com.codehows.wqproject.utils.FileHandler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final ImageRepository imageRepository;
    private final FileHandler fileHandler;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final LikesRepository likesRepository;

    @Value("${uploadPath}")
    private String uploadPath;

    public List<LectureListDto> allList() {
        return lectureRepository.findAllList();
    }

    public void regist (LectureDto lectureDto, MultipartFile img){
        String maxKey = lectureRepository.findMaxKey();
        StringBuilder nextNum = new StringBuilder(maxKey == null ? "001" : (Integer.parseInt(maxKey.split("L")[1]) + 1) + "");
        while(nextNum.length()<3)
            nextNum.insert(0, "0");
        String nextKey = "L" + nextNum;
        Lecture lecture = Lecture.createLecture(lectureDto);
        lecture.setLCode(nextKey);
        Lecture result = lectureRepository.save(lecture);
        if(img!=null) saveOrUpdateImg(img, result);
    }

    private void saveOrUpdateImg(MultipartFile file, Lecture lecture) {
        try {
            String fileName = fileHandler.uploadFile(uploadPath, file);
            Image image = imageRepository.findByLecture(lecture).orElse(null);
            if(image == null) {
                imageRepository.save(
                        Image.builder()
                                .lecture(lecture)
                                .oriImgName(fileName)
                                .imgUrl(uploadPath)
                                .build()
                );
            }else {
                fileHandler.deleteFile(image.getImgUrl() + image.getOriImgName());
                image.updateImage(fileName, uploadPath);
            }
        } catch (Exception e) {
            log.info("강의 이미지 파일 저장 실패");
        }
    }

    public LectureDto findLecture(String lCode) throws EntityNotFoundException {
        Lecture lecture = lectureRepository.findById(lCode)
                .orElseGet(() -> {
                    log.info("해당 강의 없음");
                    throw new EntityNotFoundException();
                });
        return LectureDto.of(lecture);
    }

    public Resource lectureImage(String lCode) throws EntityNotFoundException {
        Lecture lecture = lectureRepository.findById(lCode)
                .orElseGet(() -> {
                    log.info("해당 강의 없음");
                    throw new EntityNotFoundException();
                });
        Image image = imageRepository.findByLecture(lecture)
                .orElseGet(() -> {
                    log.info("강의 이미지 없음");
                    throw new EntityNotFoundException();
                });
        return new FileSystemResource(image.getImgUrl() + image.getOriImgName());
    }

    public List<LectureListDto> findFilteredList(LectureListConditionDto dto) {
        return lectureRepository.searchList(dto.getKeyword(), dto.getSdate(), dto.getEdate());
    }

    public void delete(String lCode) {
        Lecture lecture = lectureRepository.findById(lCode)
                .orElseThrow(EntityNotFoundException::new);
        List<Question> questions = questionRepository.findAllByLecture(lecture);
        for(Question q : questions) {
            answerRepository.deleteAll(answerRepository.findAllByQuestion(q));
            likesRepository.deleteAll(likesRepository.findAllByQuestion(q));
            questionRepository.delete(q);
        }
        lectureRepository.delete(lecture);
    }

    public LectureDto update(String lCode, LectureDto lectureDto, MultipartFile img) {
        Lecture lecture = lectureRepository.findById(lCode)
                .orElseThrow(EntityNotFoundException::new);
        if(img != null) saveOrUpdateImg(img, lecture);
        lecture.updateLecture(lectureDto);
        return LectureDto.of(lecture);
    }

    public Boolean setOrInit(String lCode, Boolean flag) {
        Lecture lecture = lectureRepository.findById(lCode)
                .orElseThrow(EntityNotFoundException::new);
        lecture.setOrInitActive(flag);
        return lecture.getActive();
    }

}
