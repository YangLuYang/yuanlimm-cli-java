package com.yu.tools.yuanlimm.dto.ws;

import com.yu.tools.yuanlimm.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 许愿统计信息
 */
@SuppressWarnings("WeakerAccess")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkStatisticInfo implements Serializable {
    private List<WishStockInfo> wishStockInfoList;
    private Long totalCoin;
    private Long totalStock;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishStockInfo implements Serializable {
        /**
         * 股票
         */
        private Stock stock;
        /**
         * 数量
         */
        private Long amount;
    }
}
