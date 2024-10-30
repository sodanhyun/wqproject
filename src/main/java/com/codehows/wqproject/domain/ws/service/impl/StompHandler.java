//package com.codehows.wqproject.domain.ws.service.impl;
//
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.codehows.wqproject.auth.jwt.JwtTokenProvider;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.stereotype.Component;
//
//import java.util.Objects;
//
//@RequiredArgsConstructor
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
//@Slf4j
//public class StompHandler implements ChannelInterceptor {
//
//    private final JwtTokenProvider tokenProvider;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
////        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        //TODO 쿠키 토큰 인증 방식으로 변환 필요
//        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
//            tokenProvider.validateAccessToken(token);
//        }
//        return message;
//    }
//}