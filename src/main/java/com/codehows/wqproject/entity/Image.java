package com.codehows.wqproject.entity;

import com.codehows.wqproject.auditing.BaseTimeEntity;
import com.codehows.wqproject.domain.lecture.requestDto.ImageReq;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "image")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Image extends BaseTimeEntity {
    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "lecture_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lecture lecture;

    private String oriImgName;

    private String imgUrl;

    @Builder
    Image(Lecture lecture, String oriImgName, String imgUrl) {
        this.lecture = lecture;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }

    public static Image createImage(ImageReq imageDto, Lecture lecture) {
        return Image.builder()
                .lecture(lecture)
                .oriImgName(imageDto.getOriImgName())
                .imgUrl(imageDto.getImgUrl())
                .build();
    }

    public void updateImage(String oriImgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
