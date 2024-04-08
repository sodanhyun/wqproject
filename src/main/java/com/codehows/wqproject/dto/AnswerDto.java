package com.codehows.wqproject.dto;

import com.codehows.wqproject.entity.Answer;
import com.codehows.wqproject.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnswerDto {
    private String aCode;
    private String qCode;
    private String content;
    private static ModelMapper modelMapper = new ModelMapper();

    public static AnswerDto of(Answer answer) {
        AnswerDto dto = modelMapper.map(answer, AnswerDto.class);
        dto.setQCode(answer.getQuestion().getQCode());
        return dto;
    }
}

