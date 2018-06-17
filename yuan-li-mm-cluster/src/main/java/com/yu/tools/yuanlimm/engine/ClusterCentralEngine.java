package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.entity.WorkerNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Engine - 集群管理引擎
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Slf4j
@Lazy(false)
@Component
public class ClusterCentralEngine {
    /**
     * 工作节点列表
     */
    @Getter
    private Map<String, WorkerNode> onlineWorker = new HashMap<>();
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
     * 节点名称是否存在
     *
     * @param workerNode 工作节点
     * @return 是否存在
     */
    public Boolean nodeNameExist(WorkerNode workerNode) {
        return onlineWorker.containsKey(workerNode.getName());
    }

    /**
     * 节点连接
     *
     * @param workerNode 工作节点
     */
    public void nodeConnected(WorkerNode workerNode) {
        onlineWorker.put(workerNode.getName(), workerNode);
    }

    /**
     * 节点断开
     *
     * @param workerNode 工作节点
     */
    public void nodeDisConnected(WorkerNode workerNode) {
        if (workerNode != null && StringUtils.isNotBlank(workerNode.getName())) {
            monitorEngine.workerDisconnected(workerNode);
            controlEngine.workerDisconnected(workerNode);
            onlineWorker.remove(workerNode.getName());
        }
    }
}
