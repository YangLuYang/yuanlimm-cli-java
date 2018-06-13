package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.entity.WorkerNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

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
    private List<WorkerNode> workerNodeList = new LinkedList<>();

    /**
     * 节点连接
     *
     * @param workerNode 工作节点
     */
    public void nodeConnected(WorkerNode workerNode) {
        workerNodeList.add(workerNode);
    }

    /**
     * 节点断开
     *
     * @param workerNode 工作节点
     */
    public void nodeDisConnected(WorkerNode workerNode) {
        workerNodeList.remove(workerNode);
    }
}
