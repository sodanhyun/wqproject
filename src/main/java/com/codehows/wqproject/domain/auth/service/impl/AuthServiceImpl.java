package com.codehows.wqproject.domain.auth.service.impl;

import com.codehows.wqproject.auth.jwt.RefreshTokenService;
import com.codehows.wqproject.auth.user.Role;
import com.codehows.wqproject.domain.auth.requestDto.LoginDto;
import com.codehows.wqproject.domain.account.responseDto.UserDto;
import com.codehows.wqproject.domain.auth.responseDto.TokenResponse;
import com.codehows.wqproject.domain.auth.service.AuthService;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.entity.RefreshToken;
import com.codehows.wqproject.auth.jwt.JwtTokenProvider;
import com.codehows.wqproject.auth.jwt.JwtTokenStatus;
import com.codehows.wqproject.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

import static com.codehows.wqproject.auth.jwt.JwtTokenConstant.ACCESS_TOKEN_DURATION;
import static com.codehows.wqproject.auth.jwt.JwtTokenConstant.addRefreshTokenToCookie;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public User signup(UserDto userDto) {
        if (userRepository.findById(userDto.getId()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        User user = User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.TEMP)
                .build();
        return userRepository.save(user);
    }

    //Todo 삭제할 예정
    public TokenResponse tempSignin() throws EntityNotFoundException {
        UserDto tempWsUser = UserDto.builder()
                .name("TEMP_WS_USER")
                .id("TEMP_WS_USER")
                .password("codehows")
                .role("TEMP")
                .build();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(tempWsUser.getId(), tempWsUser.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(EntityNotFoundException::new);
        String refreshToken = refreshTokenService.createNewToken(user);
        String accessToken = tokenProvider.createAccessToken(user, ACCESS_TOKEN_DURATION);
        return new TokenResponse(accessToken, refreshToken, user.getRole().getKey());
    }

    public TokenResponse signin(LoginDto loginDto) throws EntityNotFoundException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(EntityNotFoundException::new);
        String refreshToken = refreshTokenService.createNewToken(user);
        String accessToken = tokenProvider.createAccessToken(user, ACCESS_TOKEN_DURATION);
        return new TokenResponse(accessToken, refreshToken, user.getRole().getKey());
    }

    public TokenDto refresh(TokenDto tokenDto) throws RuntimeException {
        JwtTokenStatus.StatusCode tokenStatusCode = tokenProvider.validateToken(tokenDto.getRefreshToken());
        if(tokenStatusCode != JwtTokenStatus.StatusCode.OK)
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(EntityNotFoundException::new);
        if(!refreshToken.getValue().equals(tokenDto.getRefreshToken()))
            throw new RuntimeException("토큰의 유저 정보와 일치하지 않습니다.");
        TokenDto newToken = jwtTokenProvider.createToken(authentication);
        refreshToken.updateValue(newToken.getRefreshToken());
        log.info("refresh out");
        return newToken;
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findById);
    }

}
