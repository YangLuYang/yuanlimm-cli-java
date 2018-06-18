package com.yu.tools.yuanlimm.config;

import com.yu.tools.yuanlimm.engine.ClusterCentralEngine;
import com.yu.tools.yuanlimm.entity.WorkerNode;
import com.yu.tools.yuanlimm.exception.PermissionDeniedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * CostumeChannelInterceptorAdapter
 */
@Component
public class CostumeChannelInterceptorAdapter extends ChannelInterceptorAdapter implements ChannelInterceptor {

    @Resource
    private ClusterCentralEngine clusterCentralEngine;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            if (StringUtils.isNotBlank(accessor.getLogin())) {
                WorkerNode node = new WorkerNode(accessor.getLogin(), accessor.getSessionId());
                if (clusterCentralEngine.nodeNameExist(node)) {
                    System.out.println(String.format("Worker: %s name exist.\t Reject.", node.getName()));
                    return null;
                }

                accessor.setUser(node);
            } else {
                throw new PermissionDeniedException();
            }
        }
        return message;
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return super.preReceive(channel);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        super.postSend(message, channel, sent);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        super.afterSendCompletion(message, channel, sent, ex);
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return super.postReceive(message, channel);
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        super.afterReceiveCompletion(message, channel, ex);
    }
}
