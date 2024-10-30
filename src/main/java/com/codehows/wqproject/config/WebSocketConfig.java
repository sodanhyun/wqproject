package com.codehows.wqproject.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.io.IOException;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${domainName}")
    private String frontDomain;
//    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // pSub: 채택된(picked) 질문 구독
        // qSub: 질문(question) 리스트 구독
        // aSub: 진행자(admin) - 질문 구독
        config.enableSimpleBroker("/pSub", "/qSub", "/aSub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setHandshakeHandler(webSocketHandler())
                .setAllowedOriginPatterns(frontDomain);
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(stompHandler);
//    }

    @Bean
    public DefaultHandshakeHandler webSocketHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected void handleInvalidUpgradeHeader(ServerHttpRequest request, ServerHttpResponse response) throws IOException {
                // NOTE: Upgrade 헤더에 올바르지 않은 값이 전달되었을때 호출된다.
                log.error("Method: {}, URI: {}, Principal: {}, Headers: {}", request.getMethod().name(), request.getURI(), request.getPrincipal(), request.getHeaders());
                super.handleInvalidUpgradeHeader(request, response);
            }
        };
    }

}
