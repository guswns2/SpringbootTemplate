package com.jhj.template.mqtt.controller;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jhj.template.common.config.MqttConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Mqtt API", description = "MqttController")
@Slf4j
@RestController
@RequestMapping("/api6")
public class MqttController {

    @Autowired
    MqttConfig mqttConfig;

    @Autowired
    MqttClient mqttClient;

    @GetMapping("/publishMessage")
    public ResponseEntity<Void> publishMessage(@RequestParam String message) {
        // log.info(mqttClient + "");
        log.info("Publish Topic : topic1");
        log.info("Publish Message : " + message);

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes());

        try {
            mqttClient.connect(mqttConfig.mqttConnectOptions());
            mqttClient.publish("topic1", mqttMessage);
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void receiveMessage(String payload) {
        // payload처리
        log.info("Subscribe message : topic1");
        log.info("Received message : " + payload);
    }

}
