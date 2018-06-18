package com.yu.tools.yuanlimm.dto.api;

import com.yu.tools.yuanlimm.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 许愿股票信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishStockInfo implements Serializable {
    /**
     * 股票
     */
    private Stock stock;
    /**
     * 数量
     */
    private Long amount;
}
