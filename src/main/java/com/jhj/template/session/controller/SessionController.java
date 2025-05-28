package com.jhj.template.session.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Session API", description = "SessionController")
@Slf4j
@RestController
@RequestMapping("/api8")
public class SessionController {

    @GetMapping(value = "/getSession")
    public String getSession(String username, HttpServletRequest request) {

        log.debug("request : " + request);

        HttpSession session = request.getSession();
        log.debug("session : " + session);

        session.setAttribute("username", username);

        String id = session.getId();
        Object attribute = session.getAttribute("username");
        int maxInactiveInterval = session.getMaxInactiveInterval(); // 세션 유효기간 (1800 -> 30분)

        return "id: " + id + ", attribute : " + attribute + ", maxInactiveInterval : "
                + maxInactiveInterval;

    }

    @GetMapping(value = "/deleteSession")
    public String deleteSession(HttpServletRequest request) {

        try {
            HttpSession session = request.getSession();
            session.invalidate();
        } catch (Exception e) {
            log.error("deleteSession - 세션 삭제 실패", e);
            return "세션 삭제 실패";
        }

        return "세션 삭제 성공";

    }

}
