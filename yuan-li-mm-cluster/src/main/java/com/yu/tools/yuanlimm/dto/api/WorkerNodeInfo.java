package com.yu.tools.yuanlimm.dto.api;

import com.yu.tools.yuanlimm.dto.ws.WorkerMonitorInfo;
import com.yu.tools.yuanlimm.entity.WorkerNode;
import com.yu.tools.yuanlimm.enums.SystemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 工作节点信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkerNodeInfo implements Serializable {
    /**
     * 工作节点
     */
    private WorkerNode workerNode;
    /**
     * 系统状态
     */
    private SystemStatus systemStatus;
    /**
     * 监控信息
     */
    private WorkerMonitorInfo workerMonitorInfo;
}
