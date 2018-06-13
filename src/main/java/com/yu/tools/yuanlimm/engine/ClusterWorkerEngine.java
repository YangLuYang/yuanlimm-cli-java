package com.yu.tools.yuanlimm.engine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * Engine - 集群管理引擎
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Slf4j
@Lazy(false)
@Component
public class ClusterWorkerEngine {

    public static void main(String[] args) {
        String url = "ws://localhost:8080/portfolio";

        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new StringMessageConverter());
        stompClient.setTaskScheduler(taskScheduler); // for heartbeats
    }
}
