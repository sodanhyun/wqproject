package com.goormthon.halmang.auth.jwt;

import com.goormthon.halmang.utils.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.goormthon.halmang.constant.JwtTokenConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);

        if(accessToken != null && !accessToken.isEmpty() && !accessToken.equals("null")) {
            String accessTokenState = jwtTokenProvider.validateAccessToken(accessToken);
            if (accessTokenState.equals("success")) {
                log.info("Valid access token");
                Authentication authentication = jwtTokenProvider.getAuthentication(jwtTokenProvider.getUserIdInAccessToken(accessToken));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else if (accessTokenState.equals("expired")) {
                log.info("Expired access token");
                String refreshTokenState = jwtTokenProvider.validateRefreshToken(refreshToken);
                if (refreshTokenState.equals("success")) {
                    log.info("Valid refresh token");
                    Authentication authentication = tokenRefresh(response, refreshToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else if(refreshTokenState.equals("expired")) {
                    log.info("Expired refresh token");
                }else {
                    log.info("Invalid refresh token");
                }
            }
        }else if(refreshToken != null && !refreshToken.isEmpty() && !refreshToken.isBlank()) {
            log.info("Expired access token");
            String refreshTokenState = jwtTokenProvider.validateRefreshToken(refreshToken);
            if (refreshTokenState.equals("success")) {
                log.info("Valid refresh token");
                Authentication authentication = tokenRefresh(response, refreshToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else if(refreshTokenState.equals("expired")) {
                log.info("Expired refresh token");
            }else {
                log.info("Invalid refresh token");
            }
        }else {
            log.info("Expired refresh token");
        }
        filterChain.doFilter(request, response);
    }

    private Authentication tokenRefresh(HttpServletResponse response, String refreshToken) {
        String userId = jwtTokenProvider.getUserIdInRefreshToken(refreshToken);
        Authentication authentication = jwtTokenProvider.getAuthentication(userId);
        String newAccessToken = jwtTokenProvider.createJwtToken(userId, "access");
        log.info("Complete tokens renew");
        CookieUtil.addCookie(response,
                ACCESS_TOKEN_COOKIE_NAME,
                newAccessToken,
                ACCESS_TOKEN_DURATION);
        return authentication;
    }

    private String getAccessToken(HttpServletRequest request) {
        return getTokenFromCookie(request, ACCESS_TOKEN_COOKIE_NAME);
    }

    public String getRefreshToken(HttpServletRequest request) {
        return getTokenFromCookie(request, REFRESH_TOKEN_COOKIE_NAME);
    }

    private String getTokenFromCookie(HttpServletRequest request, String type) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(type)) {
                token = cookie.getValue();
                break;
            }
        }
        return token;
    }
}
