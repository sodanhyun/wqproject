package com.codehows.wqproject.domain.account.requestDto;

import com.codehows.wqproject.constant.enumVal.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountUpdateReq {
    String id;
    String name;
    String email;
    String userRole;
}
