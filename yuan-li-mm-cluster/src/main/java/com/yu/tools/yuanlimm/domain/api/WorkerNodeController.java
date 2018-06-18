package com.yu.tools.yuanlimm.domain.api;

import com.yu.tools.yuanlimm.dto.api.WorkerNodeInfo;
import com.yu.tools.yuanlimm.dto.ws.WorkerMonitorInfo;
import com.yu.tools.yuanlimm.engine.ClusterCentralEngine;
import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.engine.MonitorEngine;
import com.yu.tools.yuanlimm.entity.WorkerNode;
import com.yu.tools.yuanlimm.enums.SystemStatus;
import com.yu.tools.yuanlimm.model.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RestController - WorkerNode
 */
@RestController
@RequestMapping("/api/")
public class WorkerNodeController {
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
     * 集群中心引擎
     */
    @Resource
    private ClusterCentralEngine clusterCentralEngine;

    /**
     * 获取工作节点列表
     *
     * @return 监控信息
     */
    @GetMapping(path = "/v1/workerNode/list")
    public Message getWorkerNodeInfo() {
        Map<String, WorkerNode> onlineWorker = clusterCentralEngine.getOnlineWorker();

        List<WorkerNodeInfo> collect = onlineWorker.values().stream()
                .sorted(Comparator.comparing(WorkerNode::getName))
                .map(workerNode -> {
                    WorkerMonitorInfo workerMonitorInfo = monitorEngine.getWorkerMonitorInfo().get(workerNode.getName());
                    SystemStatus systemStatus = controlEngine.getWorkerSystemStatus().get(workerNode.getName());
                    return new WorkerNodeInfo(workerNode, systemStatus, workerMonitorInfo);
                })
                .collect(Collectors.toList());
        return Message.result(collect);
    }
}
