package com.codehows.wqproject.domain.lecture.responseDto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureLimitRes {
    private String title;
    private Integer limitMin;
    private Boolean active;
}
