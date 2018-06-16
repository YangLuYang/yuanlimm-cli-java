package com.yu.tools.yuanlimm.exception;

/**
 * Exception - 控制节点配置异常
 */
@SuppressWarnings("unused")
public class WorkerConfigException extends RuntimeException {

    /**
     * 构造函数
     */
    public WorkerConfigException() {
        super("控制节点配置异常");
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public WorkerConfigException(String message) {
        super(message);
    }
}
