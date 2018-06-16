package com.yu.tools.yuanlimm.domain;


import com.yu.tools.yuanlimm.dto.CommonWebSocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SuppressWarnings("unused")
@RestController
@MessageMapping("/ws/worker")
public class WsController {

    @MessageMapping("/status")
    @SendToUser(destinations = "/topic/status", broadcast = false)
    public void workerStatus(CommonWebSocketMessage<String> message, Principal principal) {
        System.out.println("User:" + principal + "\t Message:" + message);
    }
}
