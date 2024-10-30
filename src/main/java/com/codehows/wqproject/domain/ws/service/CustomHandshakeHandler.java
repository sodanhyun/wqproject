package com.codehows.wqproject.domain.ws.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.io.IOException;

@Slf4j
@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected void handleInvalidUpgradeHeader(ServerHttpRequest request, ServerHttpResponse response) throws IOException {
        // NOTE: Upgrade 헤더에 올바르지 않은 값이 전달되었을때 호출된다.
        log.error("Method: {}, URI: {}, Principal: {}, Headers: {}", request.getMethod().name(), request.getURI(), request.getPrincipal(), request.getHeaders());
        super.handleInvalidUpgradeHeader(request, response);
    }
}