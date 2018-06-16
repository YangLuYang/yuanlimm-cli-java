package com.yu.tools.yuanlimm.domain;

import com.yu.tools.yuanlimm.dto.MonitorInfoResponse;
import com.yu.tools.yuanlimm.engine.ComputeEngine;
import com.yu.tools.yuanlimm.engine.ControlEngine;
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

    @Resource
    private ComputeEngine computeEngine;

    @Resource
    private ControlEngine controlEngine;

    @GetMapping(path = "/v1/monitor")
    public Message getMonitorInfo() {
        Long hashSpeed = monitorEngine.getLastHashSpeed();
        Long totalHash = computeEngine.getHashCounter().get();
        Integer hard = controlEngine.getHASH_HARD();
        return Message.result(new MonitorInfoResponse(hashSpeed, hard, totalHash));
    }
}
