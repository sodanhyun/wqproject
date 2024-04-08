package com.codehows.wqproject.dto;

import com.codehows.wqproject.entity.Lecture;
import com.codehows.wqproject.entity.Question;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuestionDto {

    private String qCode;

    private String lCode;

    private String memberId;

    private Integer answerCount;

    private Integer likesCount;

    private String name;

    private String content;

    private Boolean pick;

    private Boolean myLike;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime regTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updateTime;

    @QueryProjection
    public QuestionDto(String qCode, String lCode, String memberId,
                       String name, String content, Boolean pick, Integer likesCount,
                       LocalDateTime regTime, LocalDateTime updateTime) {
        this.qCode = qCode;
        this.lCode = lCode;
        this.memberId = memberId;
        this.name = name;
        this.content = content;
        this.pick = pick;
        this.likesCount = likesCount;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }

    private static ModelMapper modelMapper = new ModelMapper();

    public static QuestionDto of(Question question) {
        QuestionDto dto = modelMapper.map(question, QuestionDto.class);
        dto.setLCode(question.getLecture().getLCode());
        dto.setName(question.getMember().getName());
        dto.setAnswerCount(question.getAnswerCount()==null ? 0 : (question.getAnswerCount()));
        dto.setLikesCount(question.getLikesCount()==null ? 0 : (question.getLikesCount()));
        dto.setMyLike(false);
        return dto;
    }
}
