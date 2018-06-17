package com.yu.tools.yuanlimm.domain.api;

import com.yu.tools.yuanlimm.dto.MonitorInfoResponse;
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

    @Resource
    private MonitorEngine monitorEngine;

    @GetMapping(path = "/v1/monitor")
    public Message getMonitorInfo() {
        MonitorInfoResponse info = monitorEngine.getMonitorInfo();
        return Message.result(info);
    }
}
