package com.jhj.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {
	// client에서 websocket 연결할 경로를 설정
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		log.info("stomp open");

		registry.addEndpoint("/stomp")
				.setAllowedOriginPatterns("*") // web에서 연결할 때 http://서버ip:서버port/엔드포인트 전체 명시
				.withSockJS(); // web에서 sockjs 사용 시
	}

	// 메시지 받을 때 관련 경로 설정
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		// 구독(수신)
		registry.enableSimpleBroker("/sub");
		// 출간(송신)
		registry.setApplicationDestinationPrefixes("/pub");

	}

	// websocket이 연결되거나, sub/pub/send 등 client에서 메시지를 보내게 될 때 interceptor를 통해 핸들링
	// @Override
	// public void configureClientInboundChannel(ChannelRegistration
	// registration) {
	// registration.interceptors(interceptor);
	// }
}
