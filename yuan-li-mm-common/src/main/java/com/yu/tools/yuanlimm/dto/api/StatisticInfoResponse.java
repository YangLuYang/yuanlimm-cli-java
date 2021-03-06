package com.yu.tools.yuanlimm.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 许愿统计信息响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticInfoResponse implements Serializable {
    private List<WishStockInfo> wishStockInfoList;
    private Long totalCoin;
    private Long totalStock;
}
