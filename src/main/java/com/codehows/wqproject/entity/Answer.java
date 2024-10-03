package com.codehows.wqproject.entity;

import com.codehows.wqproject.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "answer")
@Getter @Setter @ToString
@NoArgsConstructor
public class Answer extends BaseEntity {
    @Id
    @Column(name = "a_code")
    private String aCode;

    @ManyToOne
    @JoinColumn(name = "q_code", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Builder
    Answer(String aCode, Question question, String content) {
        this.aCode = aCode;
        this.question = question;
        this.content = content;
    }
    public void updateContent(String content) {
        this.content = content;
    }

}
