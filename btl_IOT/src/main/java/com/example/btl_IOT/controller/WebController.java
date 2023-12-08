package com.example.btl_IOT.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebController {

    @MessageMapping("/app/{topic}")
    @SendTo("/topic/{topic}")
    public String handleWebSocketMessage(String message) {
        return message;
    }
}
