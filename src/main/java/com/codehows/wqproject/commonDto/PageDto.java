package com.codehows.wqproject.commonDto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    List<T> content;
    Integer pageNumber;
    Integer totalPages;
}
