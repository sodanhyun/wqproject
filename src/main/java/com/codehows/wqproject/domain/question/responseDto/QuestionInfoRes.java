package com.codehows.wqproject.domain.question.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuestionInfoRes {

    private String qCode;

    private String lCode;

    private String content;

    private String userId;

    private String userName;

    private Integer answerCnt;

    private Integer likesCnt;

    private Boolean isPicked;

    private Boolean myLike;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime regTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updateTime;

    @QueryProjection
    public QuestionInfoRes(String qCode,
                           String lCode,
                           String content,
                           String userId,
                           String userName,
                           Integer answerCnt,
                           Integer likesCnt,
                           Boolean isPicked,
                           LocalDateTime regTime,
                           LocalDateTime updateTime) {
        this.qCode = qCode;
        this.lCode = lCode;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.answerCnt = answerCnt;
        this.likesCnt = likesCnt;
        this.isPicked = isPicked;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }
}
