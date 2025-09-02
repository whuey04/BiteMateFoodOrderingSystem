package com.bitemate.task;

import com.bitemate.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WebSocketTask {
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * Send a message to the client via WebSocket every 30 seconds.
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void sendMessageToClient() {
        webSocketServer.sendToAllClient("This is a message from the server: " + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    }
}
