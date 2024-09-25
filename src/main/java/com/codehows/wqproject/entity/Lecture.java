package com.codehows.wqproject.entity;

import com.codehows.wqproject.auditing.BaseEntity;
import com.codehows.wqproject.domain.lecture.requestDto.LectureDto;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lecture")
@Getter @Setter @ToString
@NoArgsConstructor
public class Lecture extends BaseEntity {
    @Id
    @Column(name = "l_code")
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

    @Builder
    Lecture(String title, String speaker, String location,
            LocalDateTime sdate, LocalDateTime edate, String etc,
            Boolean active, Integer limitMin) {
        this.title = title;
        this.speaker = speaker;
        this.location = location;
        this.sdate = sdate;
        this.edate = edate;
        this.etc = etc;
        this.active = active;
        this.limitMin = limitMin;
    }

    public static Lecture createLecture(LectureDto lectureDto) {
        return Lecture.builder()
                .title(lectureDto.getTitle())
                .speaker(lectureDto.getSpeaker())
                .location(lectureDto.getLocation())
                .sdate(lectureDto.getSdate())
                .edate(lectureDto.getEdate())
                .etc(lectureDto.getEtc())
                .active(false)
                .limitMin(lectureDto.getLimitMin())
                .build();
    }

    public void updateLecture(LectureDto lectureDto) {
        this.title = lectureDto.getTitle();
        this.speaker = lectureDto.getSpeaker();
        this.location = lectureDto.getLocation();
        this.sdate = lectureDto.getSdate();
        this.edate = lectureDto.getEdate();
        this.etc = lectureDto.getEtc();
        this.limitMin = lectureDto.getLimitMin();
    }

    public void setOrInitActive(Boolean flag) {
        this.active = flag;
    }
}
