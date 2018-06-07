package com.yu.tools.yuanlimm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 许愿统计信息响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticInfoResponse {
    private List<WishStockInfo> wishStockInfoList;
    private Long coinAmount;
}
