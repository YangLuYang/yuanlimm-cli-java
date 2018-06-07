package com.yu.tools.yuanlimm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 许愿统计信息响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorInfoResponse {
    private Long hashSpeed;
    private Integer hard;
    private Long totalHash;
}
