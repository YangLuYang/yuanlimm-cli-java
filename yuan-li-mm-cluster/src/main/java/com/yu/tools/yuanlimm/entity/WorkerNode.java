package com.yu.tools.yuanlimm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

/**
 * WorkerNode
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerNode implements Principal {
    /**
     * 名称
     */
    private String name;
    /**
     * 会话ID
     */
    private String sessionId;
}
