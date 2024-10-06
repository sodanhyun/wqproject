package com.codehows.wqproject.domain.lecture.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LectureActiveReq {
    private String lCode;
    private Boolean active;
}
