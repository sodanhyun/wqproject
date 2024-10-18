package com.codehows.wqproject.repository.querydsl;

import com.codehows.wqproject.domain.account.responseDto.AccountInfoRes;
import com.codehows.wqproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    Page<AccountInfoRes> getAllUsersByPaging(Pageable pageable);
}
