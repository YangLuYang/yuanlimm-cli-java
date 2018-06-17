package com.yu.tools.yuanlimm.domain.ws;

import com.yu.tools.yuanlimm.dto.CommonWebSocketMessage;
import com.yu.tools.yuanlimm.dto.WishResultInfo;
import com.yu.tools.yuanlimm.dto.WorkerMonitorInfo;
import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.engine.MonitorEngine;
import com.yu.tools.yuanlimm.engine.StatisticEngine;
import com.yu.tools.yuanlimm.entity.WorkerNode;
import com.yu.tools.yuanlimm.enums.SystemStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;

@SuppressWarnings("unused")
@RestController
@MessageMapping("/ws/worker")
public class WorkerController {

    @Resource
    private StatisticEngine statisticEngine;

    @Resource
    private MonitorEngine monitorEngine;

    @Resource
    private ControlEngine controlEngine;

    @MessageMapping("/status")
    public void workerStatus(CommonWebSocketMessage<SystemStatus> message, Principal principal) {
        controlEngine.updateWorkerSystemStatus((WorkerNode) principal, message.getData());
    }

    @MessageMapping("/monitor")
    public void workerMonitor(CommonWebSocketMessage<WorkerMonitorInfo> message, Principal principal) {
        monitorEngine.updateWorkerMonitorInfo((WorkerNode) principal, message.getData());
    }

    @MessageMapping("/wishResult")
    public void workerWishResult(CommonWebSocketMessage<WishResultInfo> message, Principal principal) {
        WishResultInfo info = message.getData();
        statisticEngine.record(info.getType(), info.getAmount(), info.getStockCode());
    }
}
