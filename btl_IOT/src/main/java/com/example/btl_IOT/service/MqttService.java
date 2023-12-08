package com.example.btl_IOT.service;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class MqttService {
	
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private MqttClient mqttClient;

    @PostConstruct
    public void initialize() {
        try {
            String broker = "tcp://broker.hivemq.com:1883";
            String clientId = "spring-boot-mqtt-client";

            mqttClient = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            mqttClient.connect(connOpts);

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    handleMqttMessage(topic, new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            mqttClient.subscribe("/prj/temp");
            mqttClient.subscribe("/prj/hum");
            mqttClient.subscribe("/prj/pre");
            mqttClient.subscribe("/prj/gas");
            mqttClient.subscribe("/prj/led");
            mqttClient.subscribe("/prj/buz");
            mqttClient.subscribe("/prj/smoke");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void handleMqttMessage(String topic, String message) {
        simpMessagingTemplate.convertAndSend("/topic" + topic, message);
    }
}
