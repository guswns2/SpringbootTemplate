package com.jhj.template.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Kafka API", description = "KafkaController")
@RequiredArgsConstructor
@RequestMapping("/api5")
@RestController
public class KafkaController {

	@Value("${spring.kafka.topic.name}")
	private String topicName;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	@GetMapping("/sendMessage")
	public ResponseEntity<Void> sendMessage(@RequestParam String message) {
		System.out.printf("Producer Topic : %s%n", topicName);
		System.out.printf("Producer Message : %s%n", message);

		kafkaTemplate.send(topicName, message);
		return ResponseEntity.ok().build();
	}

	@KafkaListener(topics = "my-topic", groupId = "consumer_group1")
	public void consumedMessage(String message) throws IOException {
		System.out.printf("Consumed Topic : %s%n", topicName);
		System.out.printf("Consumed Message : %s%n", message);
	}
}
