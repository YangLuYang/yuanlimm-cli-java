package com.yu.tools.yuanlimm.dto.extra;

import com.yu.tools.yuanlimm.model.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 股票列表响应
 */
@NoArgsConstructor
@Data
public class StocksResponse implements Serializable {
    /**
     * 股票数量
     */
    private Long count;
    /**
     * 股票列表
     */
    private List<Stock> data;
}
