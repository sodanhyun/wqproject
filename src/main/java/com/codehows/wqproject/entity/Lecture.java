package com.codehows.wqproject.entity;

import com.codehows.wqproject.auditing.BaseEntity;
import com.codehows.wqproject.domain.lecture.requestDto.LectureReq;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lecture")
@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lecture extends BaseEntity {
    @Id @Column(name = "l_code")
    private String lCode;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String speaker;
    private String location;
    private LocalDateTime sdate;
    private LocalDateTime edate;
    private String etc;
    private Boolean active;
    private Integer limitMin;

    public void updateLecture(LectureReq lectureReq) {
        this.title = lectureReq.getTitle();
        this.speaker = lectureReq.getSpeaker();
        this.location = lectureReq.getLocation();
        this.sdate = lectureReq.getSdate();
        this.edate = lectureReq.getEdate();
        this.etc = lectureReq.getEtc();
        this.limitMin = lectureReq.getLimitMin();
    }

    public void setOrInitActive(Boolean flag) {
        this.active = flag;
    }
}
