package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.dto.MonitorInfoResponse;
import com.yu.tools.yuanlimm.dto.WorkerMonitorInfo;
import com.yu.tools.yuanlimm.entity.WorkerNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Engine - 监控引擎
 */
@Slf4j
@Lazy(false)
@Component
public class MonitorEngine {

    @Getter
    private Map<String, WorkerMonitorInfo> workerMonitorInfo = new HashMap<>();

    public void workerDisconnected(WorkerNode workerNode) {
        this.workerMonitorInfo.remove(workerNode.getName());
    }

    public void updateWorkerMonitorInfo(WorkerNode workerNode, WorkerMonitorInfo workerMonitorInfo) {
        this.workerMonitorInfo.put(workerNode.getName(), workerMonitorInfo);
    }

    public MonitorInfoResponse getMonitorInfo() {
        long totalHashSpeed = workerMonitorInfo.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(info -> DateUtils.addSeconds(info.getCreateDate(), 10).after(new Date()))
                .mapToLong(WorkerMonitorInfo::getHashSpeed)
                .sum();

        int averageHard = workerMonitorInfo.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(info -> DateUtils.addSeconds(info.getCreateDate(), 10).after(new Date()))
                .mapToInt(WorkerMonitorInfo::getHard)
                .findAny()
                .orElse(0);

        long totalHash = workerMonitorInfo.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(info -> DateUtils.addSeconds(info.getCreateDate(), 10).after(new Date()))
                .mapToLong(WorkerMonitorInfo::getTotalHash)
                .sum();

        return new MonitorInfoResponse(totalHashSpeed, averageHard, totalHash);
    }
}
