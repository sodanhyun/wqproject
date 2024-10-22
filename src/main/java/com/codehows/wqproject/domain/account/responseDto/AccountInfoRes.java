package com.codehows.wqproject.domain.account.responseDto;

import com.codehows.wqproject.constant.enumVal.UserRole;
import com.codehows.wqproject.domain.auth.requestDto.UserFormDto;
import com.codehows.wqproject.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoRes {
    private String id;
    private String name;
    private String email;
    private UserRole userRole;

    private static ModelMapper modelMapper = new ModelMapper();

    public static AccountInfoRes of(User user) {
        return modelMapper.map(user, AccountInfoRes.class);
    }
}
