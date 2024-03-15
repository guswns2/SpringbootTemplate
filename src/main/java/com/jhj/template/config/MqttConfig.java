package com.jhj.template.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;

@Configuration
public class MqttConfig {

	// MqttConnectOptions빈 생성
	// MQTT브로커(mosquitto)에 연결할 때 사용할 옵션을 설정
	@Bean
	public MqttConnectOptions mqttConnectOptions() {
		MqttConnectOptions option = new MqttConnectOptions();
		// MQTT브로커의 URI를 설정. 로컬 호스트의 1883포트사용
		// 여러 개의 URI 설정도 가능하다.
		option.setServerURIs(new String[] { "tcp://localhost:1883" });
		// option.setKeepAliveInterval(10);	// 기본 연결 유지 간격(초)
		// option.setConnectionTimeout(100) 	// 기본 연결 시간 초과(초)
		// option.setCleanSession(true);	//  본 정리 세션 설정
		// option.setUserName("ad");	//  osquitto 연결에 사용할 아이디, 비밀번호
		// option.setPassword("cf".t CharArray());
		return option;
	}

	// MqttPahoClientFactory빈 생성
	// MQTT클라이언트 인스턴스 생성할 때 사용
	@Bean
	public MqttPahoClientFactory mqttPahoClientFactory() {
		// DefaultMqttPahoClientFactory클래스는 MqttPahoClientFactory인터페이스를 구현
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

		// 생성한 MqttConnectOptions빈을 사용하여 연결 옵션을 설정
		factory.setConnectionOptions(mqttConnectOptions());
		return factory;
	}

	// tcp://localhost:1883를 쓰는 MQTT브로커 URL로 "someClientId"는 MQTT클라이언트 ID
	@Bean
	public MqttClient mqttClient() throws MqttException {
		return new MqttClient("tcp://localhost:1883", "jhj");
	}

	// MQTT메세지를 구독하고 처리
	@Bean
	public MqttPahoMessageDrivenChannelAdapter messageDrivenChannelAdapter(MqttPahoClientFactory mqttPahoClientFactory) {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("avb",
				mqttPahoClientFactory, "topic1");
		adapter.setCompletionTimeout(5000); // 타임아웃 설정
		adapter.setConverter(new DefaultPahoMessageConverter()); // 메시지 컨버터 설정
		adapter.setQos(1); // Quality of Service 설정. 메세지가 최소 한 번은 도착
		adapter.setOutputChannelName("mqttInputChannel");// 출력 채널 설정
		return adapter;
	}

}