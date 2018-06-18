package com.yu.tools.yuanlimm.dto.api;

import com.yu.tools.yuanlimm.enums.WishAwardType;
import com.yu.tools.yuanlimm.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 许愿日志信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WishLogInfo implements Serializable {
    /**
     * 奖励类型
     */
    private WishAwardType type;
    /**
     * 奖励数量
     */
    private Long amount;
    /**
     * 股票
     */
    private Stock stock;
    /**
     * 时间
     */
    private Date date;
}
