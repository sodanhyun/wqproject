package com.codehows.wqproject.domain.account.service;

import com.codehows.wqproject.constant.enumVal.UserRole;
import com.codehows.wqproject.domain.account.requestDto.AccountUpdateReq;
import com.codehows.wqproject.domain.account.responseDto.AccountInfoRes;
import com.codehows.wqproject.domain.auth.requestDto.UserFormDto;
import com.codehows.wqproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    public void regist(UserFormDto userFormDto);
    public List<UserRole> getAuthorities();
    public Page<AccountInfoRes> getSearchedUsersByPaging(Pageable pageable, String keyword);
    public Page<AccountInfoRes> getUsersByPaging(Pageable pageable);
    public void updateAuthorities(AccountUpdateReq req);
    public void deleteUser(String memberId);
    public User findUserById(String userId);
}
