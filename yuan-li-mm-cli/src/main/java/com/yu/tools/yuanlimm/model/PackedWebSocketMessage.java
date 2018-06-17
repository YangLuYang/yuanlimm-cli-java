package com.yu.tools.yuanlimm.model;

import com.yu.tools.yuanlimm.dto.CommonWebSocketMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class PackedWebSocketMessage<T> implements Serializable {
    /**
     *
     */
    private String destination;
    /**
     *
     */
    private CommonWebSocketMessage<T> message;
}
