package com.codehows.wqproject.dto;

import com.codehows.wqproject.entity.Lecture;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LectureDto {
    private String lCode;

    private String title;

    private String speaker;

    private String location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime sdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime edate;

    private String etc;

    private Boolean active;

    private Integer limitMin;

    private String regTime;
    private String updateTime;
    private String createdBy;
    private String modifiedBy;

    private static ModelMapper modelMapper = new ModelMapper();

    public static LectureDto of(Lecture lecture) {
        return modelMapper.map(lecture, LectureDto.class);
    }
}
