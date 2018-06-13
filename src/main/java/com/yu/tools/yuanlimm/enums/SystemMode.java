package com.yu.tools.yuanlimm.enums;

/**
 * Enum - 系统模式
 */
public enum SystemMode {
    /**
     * 自动 (默认)
     * 自动判断启动条件, 满足则自动启动, 不满足则需要Web进一步配置
     */
    auto,
    /**
     * 控制台
     * 运行参数需通过启动参数或控制台配置, 自动运行
     */
    console,
    /**
     * 网页
     * 运行参数通过网页配置, 手动运行
     */
    web,
}
