package com.yu.tools.yuanlimm.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统配置响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigResponse implements Serializable {
    /**
     * 计算线程
     */
    private Integer computeThread;
    /**
     * 钱包地址
     */
    private String walletAddress;
    /**
     * 股票代码
     */
    private String stockCode;
    /**
     * 许愿词
     */
    private String cheerWord;
}
