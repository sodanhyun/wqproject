package com.codehows.wqproject.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.codehows.wqproject.constant.Role;
import com.codehows.wqproject.jwt.TokenProvider;
import com.codehows.wqproject.jwt.TokenStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = Objects.requireNonNull(
                    accessor.getFirstNativeHeader("Authorization")
            ).substring(7);
            TokenStatus.StatusCode tokenStatusCode = tokenProvider.validateToken(token);
            if(tokenStatusCode != TokenStatus.StatusCode.OK) {
                log.info("웹 소켓 통신 중 토큰 에러");
                throw new JWTVerificationException("비정상적인 토큰 검증");
            }
        }
        return message;
    }
}