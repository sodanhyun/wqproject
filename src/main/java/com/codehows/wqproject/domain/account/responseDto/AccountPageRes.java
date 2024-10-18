package com.codehows.wqproject.domain.account.responseDto;

import com.codehows.wqproject.commonDto.PageDto;
import com.codehows.wqproject.constant.enumVal.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AccountPageRes extends PageDto<AccountInfoRes> {
    private List<UserRole> authorities;
}
