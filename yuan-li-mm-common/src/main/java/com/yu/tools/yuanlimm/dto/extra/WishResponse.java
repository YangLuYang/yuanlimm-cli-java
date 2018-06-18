package com.yu.tools.yuanlimm.dto.extra;

import com.yu.tools.yuanlimm.enums.WishAwardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 许愿响应
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WishResponse implements Serializable {
    /**
     * 许愿结果
     */
    private Boolean success;
    /**
     * 奖励类型
     */
    private WishAwardType type;
    /**
     * 奖励数量
     */
    private Long amount;
    /**
     * 消息
     */
    private String msg;
    /**
     * 难度
     */
    private Integer hard;
    /**
     * 股票
     */
    private String stock;
}
