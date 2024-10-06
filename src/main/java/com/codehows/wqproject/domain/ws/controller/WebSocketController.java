package com.codehows.wqproject.domain.ws.controller;

import com.codehows.wqproject.auth.jwt.JwtTokenProvider;
import com.codehows.wqproject.domain.answer.requestDto.AnswerDto;
import com.codehows.wqproject.domain.question.requestDto.QuestionDto;
import com.codehows.wqproject.domain.answer.service.impl.AnswerService;
import com.codehows.wqproject.domain.question.service.impl.QuestionService;
import com.codehows.wqproject.domain.account.service.impl.AccountServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final QuestionService questionService;
    private final JwtTokenProvider tokenProvider;
    private final AnswerService answerService;
    private final AccountServiceImpl accountServiceImpl;

    private static final ConcurrentHashMap<String, String> CLIENTS = new ConcurrentHashMap<>();

    @EventListener(SessionConnectEvent.class)
    public void onConnect(SessionConnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String token = Objects.requireNonNull(
                accessor.getFirstNativeHeader("Authorization")
        ).substring(7);
        String userId = tokenProvider.getAuthentication(token).getName();
        CLIENTS.put(sessionId, userId);
        log.info("[새션 연결됨] sessionId: "+ sessionId + " userId: " + userId);
    }

    @EventListener(SessionDisconnectEvent.class)
    public void onDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String userId = CLIENTS.get(sessionId);
        CLIENTS.remove(sessionId);
        log.info("[새션 종료됨] sessionId: "+ sessionId + " userId: " + userId);
    }

//     "/pub/question"
    @MessageMapping("/question")
//     "/sub/qnaLists"
//    @SendTo("/sub/qnaLists")
    public void broadCastQ(HashMap<String, Object> request, SimpMessageHeaderAccessor accessor) {
        log.info(request.toString());
        String userId = CLIENTS.get(accessor.getSessionId());
        String lCode = request.get("lCode").toString();
        String content = request.get("content").toString();
        log.info(userId);
        try{
            QuestionDto result = questionService.regist(lCode, content, userId);
            //질문 리스트 구독자들에게 생성된 질문 전송
            simpMessagingTemplate.convertAndSend("/qSub/question/" + lCode, result);
            //진행자에게 생성된 질문 전송
            simpMessagingTemplate.convertAndSend("/aSub/question/" + lCode, result);
        }catch (RuntimeException e) {
            String error = e.getMessage();
            simpMessagingTemplate.convertAndSend("/qSub/question/" + lCode, error);
            //진행자에게 생성된 질문 전송
            simpMessagingTemplate.convertAndSend("/aSub/question/" + lCode, error);
        }
    }

//    "/pub/like"
    @MessageMapping("/like")
    public void broadCastL(HashMap<String, Object> request, SimpMessageHeaderAccessor accessor) {
        log.info(request.toString());
        String userId = CLIENTS.get(accessor.getSessionId());
        String qCode = request.get("qCode").toString();
        try{
            Boolean like = questionService.like(qCode, userId);
            request.put("like", like);
            //질문 리스트 구독자들에게 like된 질문 코드 전송
            simpMessagingTemplate.convertAndSend("/qSub/like", request);
            //진행자에게 like된 질문 코드 전송
            simpMessagingTemplate.convertAndSend("/aSub/like", request);
        }catch (EntityNotFoundException e) {
            String error = "해당 질문을 찾을 수 없습니다.";
            simpMessagingTemplate.convertAndSend("/qSub/like", error);
            simpMessagingTemplate.convertAndSend("/aSub/like", error);
        }
    }

//    "/pub/pick"
    @MessageMapping("/pick")
    public void broadCastP(HashMap<String, Object> request, SimpMessageHeaderAccessor accessor) {
        String qCode = request.get("qCode").toString();
        try{
            QuestionDto result = questionService.pick(qCode);
            //관리자(서기)에게 채택된 dto 전송
            simpMessagingTemplate.convertAndSend("/pSub/question", result);
            //진행자에게 채택 여부 알림 (진행자 화면을 여러명이 구독할 경우 고려)
            simpMessagingTemplate.convertAndSend("/aSub/question", result.getQCode());
        }catch (EntityNotFoundException e) {
            String error = "해당 질문을 찾을 수 없습니다.";
            simpMessagingTemplate.convertAndSend("/qSub/like", error);
            simpMessagingTemplate.convertAndSend("/aSub/like", error);
        }
    }

    @MessageMapping("/update")
    public void broadCastU(HashMap<String, Object> request, SimpMessageHeaderAccessor accessor) {
        String qCode = request.get("qCode").toString();
        String content = request.get("content").toString();
        try{
            questionService.update(qCode, content);
            simpMessagingTemplate.convertAndSend("/qSub/update", request);
            simpMessagingTemplate.convertAndSend("/aSub/update", request);
        }catch (RuntimeException e) {
            String error = e.getMessage();
            simpMessagingTemplate.convertAndSend("/qSub/update", error);
            simpMessagingTemplate.convertAndSend("/aSub/update", error);
        }
    }

    @MessageMapping("/delete")
    public void broadCastD(HashMap<String, Object> request, SimpMessageHeaderAccessor accessor) {
        String qCode = request.get("qCode").toString();
        try{
            questionService.delete(qCode);
            simpMessagingTemplate.convertAndSend("/qSub/delete", request);
            simpMessagingTemplate.convertAndSend("/aSub/delete", request);
        }catch (RuntimeException e) {
            simpMessagingTemplate.convertAndSend("/qSub/delete", e.getMessage());
            simpMessagingTemplate.convertAndSend("/aSub/delete", e.getMessage());
        }
    }

    // 웹소켓 답변관련 코드
    @MessageMapping("/answer")
    public void broadCastA(HashMap<String, Object> request, SimpMessageHeaderAccessor accessor) {
        String qCode = request.get("qCode").toString();
        String content = request.get("content").toString();
        try{
            AnswerDto result = answerService.regist(qCode, content);
            simpMessagingTemplate.convertAndSend("/pSub/answer/" + qCode, result);
        }catch (RuntimeException e) {
            String error = e.getMessage();
            simpMessagingTemplate.convertAndSend("/pSub/answer/" + qCode, error);
        }
    }

    @MessageMapping("/answer/update")
    public void broadCastAnswerU(HashMap<String, Object> request, SimpMessageHeaderAccessor accessor) {
        String aCode = request.get("aCode").toString();
        String content = request.get("content").toString();
        try{
            answerService.update(aCode, content);
            simpMessagingTemplate.convertAndSend("/pSub/answer/update", request);
        }catch (RuntimeException e) {
            String error = e.getMessage();
            simpMessagingTemplate.convertAndSend("/pSub/answer/update", error);
        }
    }

    @MessageMapping("/answer/delete")
    public void broadCastAnswerD(HashMap<String, Object> request, SimpMessageHeaderAccessor accessor) {
        String aCode = request.get("aCode").toString();
        String qCode = aCode.split("A")[0];
        try{
            request.put("answerCount", answerService.delete(aCode));
            request.put("qCode", qCode);
            simpMessagingTemplate.convertAndSend("/pSub/answer/delete", request);
        }catch (RuntimeException e) {
            simpMessagingTemplate.convertAndSend("/pSub/answer/delete", e.getMessage());
        }
    }
}
