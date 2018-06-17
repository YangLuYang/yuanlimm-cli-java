package com.yu.tools.yuanlimm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 许愿统计信息响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerMonitorInfo implements Serializable {
    private Long hashSpeed;
    private Integer hard;
    private Long totalHash;
    private Date createDate;
}
