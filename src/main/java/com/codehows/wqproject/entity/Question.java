package com.codehows.wqproject.entity;

import com.codehows.wqproject.auditing.BaseEntity;
import com.codehows.wqproject.domain.question.requestDto.QuestionDto;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

@Entity
@Table(name = "question")
@Getter @Setter @ToString
@NoArgsConstructor
public class Question extends BaseEntity {
    @Id
    @Column(name = "q_code")
    private String qCode;

    @ManyToOne
    @JoinColumn(name = "l_code", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lecture lecture;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Formula("(SELECT count(1) FROM answer a WHERE a.q_code = q_code)")
    private Integer answerCnt;

    @Formula("(SELECT count(1) FROM likes l WHERE l.q_code = q_code)")
    private Integer likesCnt;

    private Boolean isPicked;

    @Builder
    Question(String qCode,
             Lecture lecture,
             String content,
             User user,
             Boolean isPicked) {
        this.qCode = qCode;
        this.lecture = lecture;
        this.content = content;
        this.user = user;
        this.isPicked = isPicked;
    }

    public static Question createQuestion(QuestionDto questionDto) {
        return Question.builder()
                .qCode(questionDto.getQCode())
                .content(questionDto.getContent())
                .isPicked(questionDto.getPick())
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void pickOrInit() {
        this.isPicked = !this.isPicked;
    }
}
