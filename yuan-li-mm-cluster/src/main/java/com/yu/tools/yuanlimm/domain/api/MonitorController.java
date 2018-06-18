package com.yu.tools.yuanlimm.domain.api;

import com.yu.tools.yuanlimm.dto.api.MonitorInfoResponse;
import com.yu.tools.yuanlimm.engine.MonitorEngine;
import com.yu.tools.yuanlimm.model.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * RestController - Monitor
 */
@RestController
@RequestMapping("/api/")
public class MonitorController {
    /**
     * 监控引擎
     */
    @Resource
    private MonitorEngine monitorEngine;

    /**
     * 获取监控信息
     *
     * @return 监控信息
     */
    @GetMapping(path = "/v1/monitor")
    public Message getMonitorInfo() {
        MonitorInfoResponse info = monitorEngine.getMonitorInfo();
        return Message.result(info);
    }
}
