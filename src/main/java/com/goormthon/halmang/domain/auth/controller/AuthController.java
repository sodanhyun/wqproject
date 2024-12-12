package com.goormthon.halmang.domain.auth.controller;

import com.goormthon.halmang.domain.auth.requestDto.LoginDto;
import com.goormthon.halmang.domain.auth.responseDto.TokenRes;
import com.goormthon.halmang.domain.auth.service.AuthService;
import com.goormthon.halmang.utils.CookieUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.goormthon.halmang.constant.JwtTokenConstant.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        try{
            TokenRes res = authService.login(loginDto);
            CookieUtil.addCookie(response,
                    ACCESS_TOKEN_COOKIE_NAME,
                    res.getAccessToken(),
                    ACCESS_TOKEN_DURATION);
            CookieUtil.addCookie(response,
                    REFRESH_TOKEN_COOKIE_NAME,
                    res.getRefreshToken(),
                    REFRESH_TOKEN_DURATION);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

}