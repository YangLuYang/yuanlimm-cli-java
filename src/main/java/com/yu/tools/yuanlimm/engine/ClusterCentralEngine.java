package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.entity.WorkerNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

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
    private Map<String, WorkerNode> onlineWorkerNodeMap = new HashMap<>();

    /**
     * 节点名称是否存在
     *
     * @param workerNode 工作节点
     * @return 是否存在
     */
    public Boolean nodeNameExist(WorkerNode workerNode) {
        return onlineWorkerNodeMap.containsKey(workerNode.getName());
    }

    /**
     * 节点连接
     *
     * @param workerNode 工作节点
     */
    public void nodeConnected(WorkerNode workerNode) {
        onlineWorkerNodeMap.put(workerNode.getName(), workerNode);
    }

    /**
     * 节点断开
     *
     * @param workerNode 工作节点
     */
    public void nodeDisConnected(WorkerNode workerNode) {
        if (workerNode != null && StringUtils.isNotBlank(workerNode.getName())) {
            onlineWorkerNodeMap.remove(workerNode.getName());
        }
    }
}
