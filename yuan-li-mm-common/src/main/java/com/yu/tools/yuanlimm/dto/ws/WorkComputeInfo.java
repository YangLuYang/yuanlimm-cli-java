package com.yu.tools.yuanlimm.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Worker计算信息
 */
@SuppressWarnings("WeakerAccess")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkComputeInfo implements Serializable {
    private Long currentSpeed;
}
