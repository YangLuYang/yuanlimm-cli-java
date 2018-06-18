package com.yu.tools.yuanlimm.dto.ws;

import com.yu.tools.yuanlimm.enums.WishAwardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 许愿结果信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WishResultInfo implements Serializable {
    private WishAwardType type;
    private Long amount;
    private String stockCode;
}
