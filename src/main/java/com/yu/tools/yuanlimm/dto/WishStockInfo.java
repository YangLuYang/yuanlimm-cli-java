package com.yu.tools.yuanlimm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 许愿股票信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishStockInfo {
    /**
     * 股票
     */
    private Stock stock;
    /**
     * 数量
     */
    private Long amount;
}
