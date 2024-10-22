package com.codehows.wqproject.domain.auth.controller;

import com.codehows.wqproject.domain.auth.requestDto.LoginDto;
import com.codehows.wqproject.domain.auth.responseDto.TokenRes;
import com.codehows.wqproject.domain.auth.service.AuthService;
import com.codehows.wqproject.domain.auth.service.impl.AuthServiceImpl;
import com.codehows.wqproject.utils.CookieUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.codehows.wqproject.constant.JwtTokenConstant.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

//    @GetMapping("/tempToken")
//    public ResponseEntity<?> tempToken() {
//        try{
//            HashMap<String, Object> result = authService.tempSignin();
//            TokenDto tokenDto = (TokenDto)(result.get("tokenDto"));
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER,
//                    "Bearer " + tokenDto.getAccessToken());
//            return new ResponseEntity<>(result, httpHeaders, HttpStatus.OK);
//        }catch (RuntimeException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        try{
            TokenRes res = authService.login(loginDto);
            CookieUtil.addCookie(response,
                    ACCESS_TOKEN_COOKIE_NAME,
                    res.getAccessToken(),
                    REFRESH_TOKEN_DURATION);
            CookieUtil.addCookie(response,
                    REFRESH_TOKEN_COOKIE_NAME,
                    res.getRefreshToken(),
                    REFRESH_TOKEN_DURATION);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN_COOKIE_NAME);
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.deleteCookie(request, response, TYPE);
        authService.invalidRefreshToken(authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}