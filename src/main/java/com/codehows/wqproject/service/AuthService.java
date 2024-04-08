package com.codehows.wqproject.service;

import com.codehows.wqproject.constant.Role;
import com.codehows.wqproject.dto.LoginDto;
import com.codehows.wqproject.dto.MemberDto;
import com.codehows.wqproject.dto.TokenDto;
import com.codehows.wqproject.entity.Member;
import com.codehows.wqproject.entity.RefreshToken;
import com.codehows.wqproject.jwt.TokenProvider;
import com.codehows.wqproject.jwt.TokenStatus;
import com.codehows.wqproject.repository.member.MemberRepository;
import com.codehows.wqproject.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Member signup(MemberDto memberDto) {
        if (memberRepository.findById(memberDto.getId()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        // 유저 정보를 만들어서 save
        Member member = Member.builder()
                .id(memberDto.getId())
                .name(memberDto.getName())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .role(Role.TEMP)
                .build();

        return memberRepository.save(member);
    }

    public HashMap<String, Object> tempSignin() throws EntityNotFoundException {
        MemberDto tempWsUser = MemberDto.builder()
                .name("TEMP_WS_USER")
                .id("TEMP_WS_USER")
                .password("codehows")
                .role("TEMP")
                .build();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(tempWsUser.getId(), tempWsUser.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.createToken(authentication);
        HashMap<String, Object> result = new HashMap<>();
        result.put("tokenDto", tokenDto);
        result.put("memberId", tempWsUser.getId());
        result.put("role", Role.TEMP);
        return result;
    }

    public HashMap<String, Object> signin(LoginDto loginDto) throws EntityNotFoundException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());
        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 해당 객체를 SecurityContextHolder에 저장하고
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
        TokenDto tokenDto = tokenProvider.createToken(authentication);
        Member member = memberRepository.findById(loginDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
        RefreshToken refreshToken = refreshTokenRepository.findById(loginDto.getId())
                .orElse(null);
        if(refreshToken == null) {
            refreshTokenRepository.save(RefreshToken.builder()
                    .member(member)
                    .value(tokenDto.getRefreshToken())
                    .build());
        }else {
            refreshToken.updateValue(tokenDto.getRefreshToken());
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("tokenDto", tokenDto);
        result.put("memberId", member.getId());
        result.put("role", member.getRole());
        return result;
    }

    public TokenDto refresh(TokenDto tokenDto) throws RuntimeException {
        TokenStatus.StatusCode tokenStatusCode = tokenProvider.validateToken(tokenDto.getRefreshToken());
        if(tokenStatusCode != TokenStatus.StatusCode.OK)
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        Authentication authentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(EntityNotFoundException::new);
        if(!refreshToken.getValue().equals(tokenDto.getRefreshToken()))
            throw new RuntimeException("토큰의 유저 정보와 일치하지 않습니다.");
        TokenDto newToken = tokenProvider.createToken(authentication);
        refreshToken.updateValue(newToken.getRefreshToken());
        log.info("refresh out");
        return newToken;
    }

    public Optional<Member> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findById);
    }

}
