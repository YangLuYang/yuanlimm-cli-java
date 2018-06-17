package com.yu.tools.yuanlimm.handler;

import com.yu.tools.yuanlimm.engine.ClusterWorkerEngine;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * CustomStompSessionHandler
 */
@Component
public class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    @Resource
    private ClusterWorkerEngine clusterWorkerEngine;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        clusterWorkerEngine.setSession(session);

        clusterWorkerEngine.startListenConfig();
        clusterWorkerEngine.startSendMonitor();
        clusterWorkerEngine.startSendStatus();
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        super.handleException(session, command, headers, payload, exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        super.handleTransportError(session, exception);
        if (exception instanceof ConnectionLostException) {
            System.out.println("连接断开，程序即将退出");
            System.exit(1);
        }
    }
}