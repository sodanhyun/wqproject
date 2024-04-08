package com.codehows.wqproject.dto;

import com.codehows.wqproject.entity.Member;
import com.codehows.wqproject.entity.Question;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    @NotNull
    private String id;

    @NotNull
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;

    private String regTime;
    private String updateTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public static MemberDto of(Member member) {
        return modelMapper.map(member, MemberDto.class);
    }
}
