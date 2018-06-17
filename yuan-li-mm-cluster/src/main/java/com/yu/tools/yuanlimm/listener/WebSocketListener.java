package com.yu.tools.yuanlimm.listener;

import com.yu.tools.yuanlimm.engine.ClusterCentralEngine;
import com.yu.tools.yuanlimm.entity.WorkerNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Optional;

/**
 * WebSocketListener
 */
@Slf4j
@Component
public class WebSocketListener {

    @Resource
    private ClusterCentralEngine clusterCentralEngine;

    @EventListener
    public void SessionConnectEventListener(SessionConnectEvent event) {
        log.debug(String.format("User %s connecting. SessionId: %s", Optional.ofNullable(event)
                        .map(AbstractSubProtocolEvent::getUser)
                        .map(Principal::getName)
                        .orElse("N/A"),
                event.getMessage().getHeaders().get("simpSessionId")));

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);
        clusterCentralEngine.nodeConnected((WorkerNode) accessor.getUser());
    }

    @EventListener
    public void SessionConnectedEventListener(SessionConnectedEvent event) {
        log.debug(String.format("User %s connected. SessionId: %s", Optional.ofNullable(event)
                        .map(AbstractSubProtocolEvent::getUser)
                        .map(Principal::getName)
                        .orElse("N/A"),
                event.getMessage().getHeaders().get("simpSessionId")));
    }

    @EventListener
    public void SessionDisconnectEventListener(SessionDisconnectEvent event) {
        log.debug(String.format("User %s disconnect. SessionId: %s", Optional.ofNullable(event)
                        .map(AbstractSubProtocolEvent::getUser)
                        .map(Principal::getName)
                        .orElse("N/A"),
                event.getMessage().getHeaders().get("simpSessionId")));

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);
        clusterCentralEngine.nodeDisconnected((WorkerNode) accessor.getUser());
    }

    @EventListener
    public void SessionSubscribeEventListener(SessionSubscribeEvent event) {
        log.debug(String.format("User %s subscribe. Destination: %s. SessionId: %s. SubscriptionId: %s", Optional.ofNullable(event)
                        .map(AbstractSubProtocolEvent::getUser)
                        .map(Principal::getName)
                        .orElse("N/A"),
                event.getMessage().getHeaders().get("simpDestination"),
                event.getMessage().getHeaders().get("simpSessionId"),
                event.getMessage().getHeaders().get("simpSubscriptionId")));
    }

    @EventListener
    public void SessionUnsubscribeEventListener(SessionUnsubscribeEvent event) {
        log.debug(String.format("User %s unsubscribe. SessionId: %s.\t SubscriptionId: %s", Optional.ofNullable(event)
                        .map(AbstractSubProtocolEvent::getUser)
                        .map(Principal::getName)
                        .orElse("N/A"),
                event.getMessage().getHeaders().get("simpSessionId"),
                event.getMessage().getHeaders().get("simpSubscriptionId")));
    }
}
