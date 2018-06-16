package com.yu.tools.yuanlimm.dto;

import com.yu.tools.yuanlimm.enums.WsMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonWebSocketMessage<T> {
    /**
     * 类型
     */
    private WsMessageType type;
    /**
     * 数据
     */
    private T data;
}
