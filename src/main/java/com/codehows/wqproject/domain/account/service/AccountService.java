package com.codehows.wqproject.domain.account.service;

import com.codehows.wqproject.domain.account.responseDto.AccountInfoRes;
import com.codehows.wqproject.domain.auth.requestDto.UserFormDto;
import com.codehows.wqproject.entity.User;

import java.util.HashMap;
import java.util.List;

public interface AccountService {
    public void regist(UserFormDto userFormDto);
    public List<String> getAuthorities();
    public List<AccountInfoRes> getUsers();
    public void updateAuthorities(String memberId, String memberRole);
    public void deleteMember(String memberId);
}
