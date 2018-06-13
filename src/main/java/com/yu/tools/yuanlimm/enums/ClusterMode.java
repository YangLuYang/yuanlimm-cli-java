package com.yu.tools.yuanlimm.enums;

/**
 * Enum - 集群模式
 */
public enum ClusterMode {
    /**
     * 中心节点
     * 负载整个集群的监控与配置, 不参与计算
     */
    central,
    /**
     * 工作节点
     * 负载本节点的计算, 接受中心节点的统一管理
     */
    worker,
    /**
     * 无 (默认)
     * 无集群模式, 单机运行
     */
    none
}
