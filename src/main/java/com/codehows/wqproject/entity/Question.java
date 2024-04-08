package com.codehows.wqproject.entity;

import com.codehows.wqproject.dto.QuestionDto;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import java.time.LocalDate;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Formula("(SELECT count(1) FROM answer a WHERE a.q_code = q_code)")
    private Integer answerCount;

    @Formula("(SELECT count(1) FROM likes l WHERE l.q_code = q_code)")
    private Integer likesCount;

    private String name;

    @Column(nullable = false)
    private String content;

    private Boolean pick;

    @Builder
    Question(String qCode, Lecture lecture, Member member,
             String name, String content, Boolean pick) {
        this.qCode = qCode;
        this.lecture = lecture;
        this.member = member;
        this.name = name;
        this.content = content;
        this.pick = pick;
    }

    public static Question createQuestion(QuestionDto questionDto) {
        return Question.builder()
                .qCode(questionDto.getQCode())
                .name(questionDto.getName())
                .content(questionDto.getContent())
                .pick(questionDto.getPick())
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void pickOrInit() {
        this.pick = !this.pick;
    }

    public void setNoMember() {
        this.member = null;
        this.name = "anonymous";
    }
}
