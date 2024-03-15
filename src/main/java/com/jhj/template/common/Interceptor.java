package com.jhj.template.common;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Interceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // 요청이 Controller에 도달하기 전에 실행되는 메서드
    // true를 반환하면 요청이 계속 진행되고, false를 반환하면 요청을 중단합니다.
    log.info("preHandle", request, response, handler);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    // Controller가 실행된 후, View가 렌더링되기 전에 실행되는 메서드
    log.info("postHandle", request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // View가 렌더링된 후에 실행되는 메서드
    // 이미 응답이 클라이언트로 전송된 후
    log.info("afterCompletion", request, response, handler, ex);
  }

}
