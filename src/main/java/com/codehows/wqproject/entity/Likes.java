package com.codehows.wqproject.entity;

import com.codehows.wqproject.constant.LikeId;
import com.codehows.wqproject.dto.QuestionDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "likes")
@Getter @Setter @ToString
@NoArgsConstructor
@IdClass(LikeId.class)
public class Likes extends BaseEntity {

    @Id
    @Column(name = "q_code")
    private String qCode;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "q_code")
    private Question question;

    @Id
    @Column(name = "email")
    private String email;

    @Builder
    Likes(String qCode, Question question, String email) {
        this.qCode = qCode;
        this.question = question;
        this.email = email;
    }
}
