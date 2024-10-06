package com.codehows.wqproject.domain.account.service;

import com.codehows.wqproject.domain.auth.requestDto.UserFormDto;

import java.util.HashMap;
import java.util.List;

public interface AccountService {

    public List<String> getAuthorities();
    public List<UserFormDto> getUsers();
    public void updateAuthorities(String memberId, String memberRole);
    public void deleteMember(String memberId);
}
