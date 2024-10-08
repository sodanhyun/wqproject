package com.codehows.wqproject.domain.auth.service.impl;

import com.codehows.wqproject.auth.jwt.JwtTokenProvider;
import com.codehows.wqproject.domain.auth.requestDto.LoginDto;
import com.codehows.wqproject.domain.auth.responseDto.TokenRes;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.repository.RefreshTokenRepository;
import com.codehows.wqproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

//    public HashMap<String, Object> tempSignin() throws EntityNotFoundException {
//        UserFormDto tempWsUser = UserFormDto.builder()
//                .name("TEMP_WS_USER")
//                .id("TEMP_WS_USER")
//                .password("codehows")
//                .role("TEMP")
//                .build();
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(tempWsUser.getId(), tempWsUser.getPassword());
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        TokenDto tokenDto = tokenProvider.createAccessToken(authentication);
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("tokenDto", tokenDto);
//        result.put("memberId", tempWsUser.getId());
//        result.put("role", Role.TEMP);
//        return result;
//    }

    public TokenRes login(LoginDto loginDto) throws EntityNotFoundException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        User user = userRepository.findById(authentication.getName()).orElseThrow(EntityNotFoundException::new);
        String accessToken = tokenProvider.createJwtToken(user.getId(), "access");
        String refreshToken = tokenProvider.createJwtToken(user.getId(), "refresh");
        return TokenRes.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userRole(user.getUserRole())
                .build();
    }

//    public TokenDto refresh(TokenDto tokenDto) throws RuntimeException {
//        TokenStatus.StatusCode tokenStatusCode = tokenProvider.validateToken(tokenDto.getRefreshToken());
//        if(tokenStatusCode != TokenStatus.StatusCode.OK)
//            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
//        Authentication authentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());
//        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
//                .orElseThrow(EntityNotFoundException::new);
//        if(!refreshToken.getValue().equals(tokenDto.getRefreshToken()))
//            throw new RuntimeException("토큰의 유저 정보와 일치하지 않습니다.");
//        TokenDto newToken = tokenProvider.createToken(authentication);
//        refreshToken.updateValue(newToken.getRefreshToken());
//        log.info("refresh out");
//        return newToken;
//    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

        public Optional<User> getMyUserWithAuthorities() {
            return SecurityUtil.getCurrentUsername()
                    .flatMap(userRepository::findById);
        }

    }
