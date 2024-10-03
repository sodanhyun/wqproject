package com.codehows.wqproject.domain.auth.requestDto;

import com.codehows.wqproject.constant.SocialType;
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
public class UserFormDto {

    @NotNull
    private String id;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "이름을 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    //Todo request DTO / response DTO 분리 필요
    private String role;

    private String regTime;

    private String updateTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public static UserFormDto of(User member) {
        return modelMapper.map(member, UserFormDto.class);
    }

    public void setEmail(String email) {
        this.id = email + "_" + SocialType.OWN.getKey();
        this.email = email;
    }
}
