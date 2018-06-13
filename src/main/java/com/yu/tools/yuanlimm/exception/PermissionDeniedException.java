package com.yu.tools.yuanlimm.exception;

/**
 * Exception - 权限异常
 */
@SuppressWarnings("unused")
public class PermissionDeniedException extends RuntimeException {

    /**
     * 构造函数
     */
    public PermissionDeniedException() {
        super("权限异常");
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public PermissionDeniedException(String message) {
        super(message);
    }
}
