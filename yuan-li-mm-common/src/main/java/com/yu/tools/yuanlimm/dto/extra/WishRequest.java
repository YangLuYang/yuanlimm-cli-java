package com.yu.tools.yuanlimm.dto.extra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 许愿请求
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WishRequest implements Serializable {
    /**
     * 许愿词
     */
    private String cheer_word;
    /**
     * 钱包地址
     */
    private String address;
    /**
     * Love Power
     */
    private Long love_power;
    /**
     * 股票代码
     */
    private String code;
}
