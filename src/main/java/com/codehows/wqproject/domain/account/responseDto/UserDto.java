package com.codehows.wqproject.domain.account.responseDto;

import com.codehows.wqproject.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

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

    public static UserDto of(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
