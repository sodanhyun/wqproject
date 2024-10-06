package com.codehows.wqproject.domain.account.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUpdateReq {
    String memberId;
    String memberRole;
}
