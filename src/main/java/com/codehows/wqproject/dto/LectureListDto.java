package com.codehows.wqproject.dto;

import com.codehows.wqproject.entity.Lecture;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LectureListDto {

    private String lCode;

    private String title;

    private LocalDateTime sdate;

    private LocalDateTime edate;

    private Boolean active;

    private String regTime;
    private String updateTime;
    private String createdBy;
    private String modifiedBy;

    @QueryProjection
    public LectureListDto(String lCode, String title, LocalDateTime sdate, LocalDateTime edate, Boolean active) {
        this.lCode = lCode;
        this.title = title;
        this.sdate = sdate;
        this.edate = edate;
        this.active = active;
    }

    private static ModelMapper modelMapper = new ModelMapper();

    public static LectureListDto of(Lecture lecture) {
        return modelMapper.map(lecture, LectureListDto.class);
    }
}
