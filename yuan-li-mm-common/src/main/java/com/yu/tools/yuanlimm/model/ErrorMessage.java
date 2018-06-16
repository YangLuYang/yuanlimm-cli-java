package com.yu.tools.yuanlimm.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 错误信息
 */
@Getter
@Setter
public class ErrorMessage extends Message implements Serializable {
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误详情
     */
    private String detail;
}
