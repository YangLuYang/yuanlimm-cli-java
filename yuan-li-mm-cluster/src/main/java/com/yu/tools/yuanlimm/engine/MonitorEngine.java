package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.dto.api.MonitorInfoResponse;
import com.yu.tools.yuanlimm.dto.ws.WorkerMonitorInfo;
import com.yu.tools.yuanlimm.entity.WorkerNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Engine - 监控引擎
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
@Slf4j
@Lazy(false)
@Component
public class MonitorEngine {
    /**
     * Worker监控信息
     */
    @Getter
    private Map<String, WorkerMonitorInfo> workerMonitorInfo = new HashMap<>();
    /**
     * 上次Hash速度
     */
    private Long lastHashSpeed = 0L;
    /**
     * 上次Hash计数
     */
    private Long lastHashCount = 0L;
    /**
     * 当前Hash计数
     */
    @Getter
    private AtomicLong currentHashCount = new AtomicLong(0L);

    @Resource
    private ControlEngine controlEngine;

    /**
     * Worker断开连接
     *
     * @param workerNode Worker节点
     */
    public void workerDisconnected(WorkerNode workerNode) {
        this.workerMonitorInfo.remove(workerNode.getName());
    }

    /**
     * 更新Worker监控信息
     *
     * @param workerNode        Worker节点
     * @param workerMonitorInfo 监控信息
     */
    public void updateWorkerMonitorInfo(WorkerNode workerNode, WorkerMonitorInfo workerMonitorInfo) {
        this.workerMonitorInfo.put(workerNode.getName(), workerMonitorInfo);
    }

    /**
     * 统计
     */
    @Scheduled(fixedDelay = 1000L)
    public void statistic() {
        Long currentHashCount = this.currentHashCount.get();

        lastHashSpeed = (currentHashCount - lastHashCount);
        lastHashCount = currentHashCount;
    }

    /**
     * 获取监控信息
     *
     * @return 监控信息
     */
    public MonitorInfoResponse getMonitorInfo() {
        Integer hard = controlEngine.getHASH_HARD();
        return new MonitorInfoResponse(this.lastHashSpeed, hard, this.lastHashCount);
    }
}
