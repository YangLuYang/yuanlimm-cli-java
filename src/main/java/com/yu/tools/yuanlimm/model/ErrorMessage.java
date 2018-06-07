package com.yu.tools.yuanlimm.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 错误信息
 */
@Getter
@Setter
public class ErrorMessage extends Message {
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误详情
     */
    private String detail;
}
