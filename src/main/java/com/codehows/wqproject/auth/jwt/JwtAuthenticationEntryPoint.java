package com.codehows.wqproject.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.info("JwtAuthenticationEntryPoint");

        log.error("Responding with unauthorized error. Message - {}", e.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> result = new HashMap<>();

        result.put("code", 200);
        result.put("status", "fail");
        result.put("msg", "인증정보가 정확하지 않습니다");
        result.put("data", "");

        String resultString = objectMapper.writeValueAsString(result);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        response.getWriter().write(resultString);
    }
}