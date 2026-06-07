package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/ws/{sid}")
@Slf4j
@Component
public class WebSocketServer {
    // 存放所有会话
    private static Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 会话开启
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("WS: 客户端 {} 建立连接", sid);
        sessionMap.put(sid, session);
    }

    /**
     * 接收消息
     * @param message
     * @param sid
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("WS: 收到客户端 {} 的消息: {}", sid, message);
    }

    /**
     * 会话关闭
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("WS: 断开连接 {}", sid);
        sessionMap.remove(sid);
    }

    /**
     * 给所有会话发送消息
     * @param message
     */
    public void sendToAll(String message) {
        sessionMap.forEach((sid, session) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
