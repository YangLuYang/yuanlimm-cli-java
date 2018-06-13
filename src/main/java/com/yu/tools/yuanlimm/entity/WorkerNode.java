package com.yu.tools.yuanlimm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.Principal;

/**
 * WorkerNode
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerNode implements Principal {
    /**
     * Id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 会话ID
     */
    private String sessionId;
}
