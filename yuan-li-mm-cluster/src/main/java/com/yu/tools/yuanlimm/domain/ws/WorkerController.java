package com.yu.tools.yuanlimm.domain.ws;

import com.yu.tools.yuanlimm.dto.ws.CommonWebSocketMessage;
import com.yu.tools.yuanlimm.dto.ws.WishResultInfo;
import com.yu.tools.yuanlimm.dto.ws.WorkerMonitorInfo;
import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.engine.MonitorEngine;
import com.yu.tools.yuanlimm.engine.StatisticEngine;
import com.yu.tools.yuanlimm.entity.WorkerNode;
import com.yu.tools.yuanlimm.enums.SystemStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * Worker
 */
@SuppressWarnings("unused")
@RestController
@MessageMapping("/ws/worker")
public class WorkerController {
    /**
     * 统计引擎
     */
    @Resource
    private StatisticEngine statisticEngine;
    /**
     * 监控引擎
     */
    @Resource
    private MonitorEngine monitorEngine;
    /**
     * 控制引擎
     */
    @Resource
    private ControlEngine controlEngine;

    /**
     * Worker状态
     *
     * @param message   消息
     * @param principal principal
     */
    @MessageMapping("/status")
    public void workerStatus(CommonWebSocketMessage<SystemStatus> message, Principal principal) {
        controlEngine.updateWorkerSystemStatus((WorkerNode) principal, message.getData());
    }

    /**
     * Worker监控
     *
     * @param message   消息
     * @param principal principal
     */
    @MessageMapping("/monitor")
    public void workerMonitor(CommonWebSocketMessage<WorkerMonitorInfo> message, Principal principal) {
        monitorEngine.updateWorkerMonitorInfo((WorkerNode) principal, message.getData());
    }

    /**
     * Worker许愿结果
     *
     * @param message   消息
     * @param principal principal
     */
    @MessageMapping("/wishResult")
    public void workerWishResult(CommonWebSocketMessage<WishResultInfo> message, Principal principal) {
        WishResultInfo info = message.getData();
        statisticEngine.record(info.getType(), info.getAmount(), info.getStockCode());
    }
}
