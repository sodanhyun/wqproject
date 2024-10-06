package com.codehows.wqproject.domain.lecture.responseDto;

import com.codehows.wqproject.entity.Lecture;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LectureRes {
    private String lCode;
    private String title;
    private String speaker;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime sdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime edate;
    private Integer limitMin;
    private String etc;
    private String imagePath;
    private Boolean active;

    private static ModelMapper modelMapper = new ModelMapper();

    public static LectureRes of(Lecture lecture) {
        return modelMapper.map(lecture, LectureRes.class);
    }
}
