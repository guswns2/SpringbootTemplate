package com.jhj.template.handler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

  // 세션 담는 변수
  private static final ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<String, WebSocketSession>();

  // 사용자가 웹소켓 접속 시 동작
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("afterConnectionEstablished");

    // 생성된 세션을 변수에 저장
    clients.put(session.getId(), session);
  }

  // 웹소켓 접속 종료 시 동작
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("afterConnectionClosed");

    // 변수에서 세션 제거
    clients.remove(session.getId());
  }

  // 사용자의 메세지를 받을 때 동작
  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    log.info("handleTextMessage");

    String id = session.getId(); // 메시지를 보낸 아이디
    clients.entrySet().forEach(user -> {
      if (!user.getKey().equals(id)) { // 같은 아이디가 아니면 메시지를 전달합니다.
        try {
          user.getValue().sendMessage(message);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
