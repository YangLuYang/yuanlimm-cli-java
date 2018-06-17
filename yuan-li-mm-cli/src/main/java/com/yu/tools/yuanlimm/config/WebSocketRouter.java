package com.yu.tools.yuanlimm.config;

/**
 * WebSocket路由
 */
public class WebSocketRouter {
    /**
     * 接收 - Worker配置
     */
    public static final String RECEIVE_WORKER_CONFIG = "/user/topic/worker/config";
    /**
     * 发送 - Worker许愿结果
     */
    public static final String SEND_WORKER_WISH_RESULT = "/topic/ws/worker/wishResult";
    /**
     * 发送 - Worker状态
     */
    public static final String SEND_WORKER_STATUS = "/topic/ws/worker/status";
    /**
     * 发送 - Worker监控
     */
    public static final String SEND_WORKER_MONITOR = "/topic/ws/worker/monitor";
    /**
     * 发送 - Worker统计
     */
    public static final String SEND_WORKER_STATISTIC = "/topic/ws/worker/statistic";
}
