package com.yu.tools.yuanlimm.exception;

/**
 * Exception - 业务异常
 */
@SuppressWarnings("unused")
public class BusinessException extends RuntimeException {

    /**
     * 构造函数
     */
    public BusinessException() {
        super("业务异常");
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public BusinessException(String message) {
        super(message);
    }
}
