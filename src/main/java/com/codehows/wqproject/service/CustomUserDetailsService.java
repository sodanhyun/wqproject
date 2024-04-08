package com.codehows.wqproject.service;

import com.codehows.wqproject.constant.CustomUserDetails;
import com.codehows.wqproject.entity.Member;
import com.codehows.wqproject.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    // 로그인시에 DB에서 유저정보와 권한정보를 가져와서 해당 정보를 기반으로 userdetails.User 객체를 생성해 리턴
    public UserDetails loadUserByUsername(final String username) {

        Member member = memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        return new CustomUserDetails(member);
    }

    private org.springframework.security.core.userdetails.User createUser(String username, Member member) {

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().getKey());

        return new org.springframework.security.core.userdetails.User(member.getName(),
                member.getPassword(),
                Collections.singleton(grantedAuthority));
    }
}