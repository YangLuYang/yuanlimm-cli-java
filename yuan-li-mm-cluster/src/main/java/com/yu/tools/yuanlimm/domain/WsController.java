package com.yu.tools.yuanlimm.domain;

import com.yu.tools.yuanlimm.dto.CommonWebSocketMessage;
import com.yu.tools.yuanlimm.dto.WorkStatisticInfo;
import com.yu.tools.yuanlimm.dto.WorkerMonitorInfo;
import com.yu.tools.yuanlimm.enums.SystemStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SuppressWarnings("unused")
@RestController
@MessageMapping("/ws/worker")
public class WsController {

    @MessageMapping("/status")
    public void workerStatus(CommonWebSocketMessage<SystemStatus> message, Principal principal) {
        System.out.println("User:" + principal + "\t Message:" + message);
    }

    @MessageMapping("/monitor")
    public void workerMonitor(CommonWebSocketMessage<WorkerMonitorInfo> message, Principal principal) {
        System.out.println("User:" + principal + "\t Message:" + message);
    }

    @MessageMapping("/statistic")
    public void workerStatistic(CommonWebSocketMessage<WorkStatisticInfo> message, Principal principal) {
        System.out.println("User:" + principal + "\t Message:" + message);
    }
}
