package com.codehows.wqproject.domain.lecture.requestDto;

import com.codehows.wqproject.entity.Lecture;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LectureReq {
    private String lCode;
    private String title;
    private String speaker;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime sdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime edate;
    private String etc;
    private Integer limitMin;

    private static ModelMapper modelMapper = new ModelMapper();

    public static LectureReq of(Lecture lecture) {
        return modelMapper.map(lecture, LectureReq.class);
    }
}
