package com.codehows.wqproject.domain.auth.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull
    private String id;

    @NotNull
    private String password;
}