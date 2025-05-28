package com.jhj.template.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.jhj.template.dto.Socket;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class StompController {

    @MessageMapping("/{roomId}") // 해당 경로로 메세지 수신
    @SendTo("/sub/{roomId}") // 해당 경로로 메세지 송신
    public Socket socketHandler(Socket socket) {
        String roomId = socket.getRoomId();
        String sender = socket.getSender();
        String msg = socket.getMsg();
        log.info("# roomId = {}", roomId);
        log.info("# message = {}", sender);
        log.info("# message = {}", msg);

        socket.setMsg(socket.getSender() + "님이 입장하였습니다.");

        Socket result = new Socket(roomId, sender, msg);
        return result;
    }
}
