package com.codehows.wqproject.commonDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class PageDto<T> {
    Page<T> page;

    Integer maxPageNum;
}
