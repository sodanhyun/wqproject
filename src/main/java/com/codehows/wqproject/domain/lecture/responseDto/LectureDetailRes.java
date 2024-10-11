package com.codehows.wqproject.domain.lecture.responseDto;

import com.codehows.wqproject.entity.Lecture;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LectureDetailRes extends LectureInfoRes {
    private String speaker;
    private String location;
    private Integer limitMin;
    private String etc;
    private String imagePath;

    private static ModelMapper modelMapper = new ModelMapper();

    public static LectureDetailRes of(Lecture lecture) {
        return modelMapper.map(lecture, LectureDetailRes.class);
    }
}
