package com.sky.task;

import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class WebSocketTask {

    @Autowired
    private WebSocketServer webSocketServer;

//    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessageTask() {
        webSocketServer.sendToAll("测试定时消息: " + LocalDateTime.now());
    }
}
