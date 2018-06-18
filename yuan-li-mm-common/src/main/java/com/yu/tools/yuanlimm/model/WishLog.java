package com.yu.tools.yuanlimm.model;

import com.yu.tools.yuanlimm.enums.WishAwardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 许愿日志
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WishLog implements Serializable {
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
    private String stockCode;
    /**
     * 时间
     */
    private Date date;
}
