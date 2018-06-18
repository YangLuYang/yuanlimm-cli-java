package com.yu.tools.yuanlimm.domain;

import com.yu.tools.yuanlimm.dto.api.SystemConfigRequest;
import com.yu.tools.yuanlimm.dto.api.SystemConfigResponse;
import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.enums.SystemStatus;
import com.yu.tools.yuanlimm.model.Message;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * RestController - System
 */
@RestController
@RequestMapping("/api/")
public class SystemController {

    @Resource
    private ControlEngine controlEngine;

    @GetMapping(path = "/v1/system/status")
    public Message getSystemStatus() {
        SystemStatus status = controlEngine.getSYSTEM_STATUS();
        return Message.result(status);
    }

    @PutMapping(path = "/v1/system/service/start")
    public Message startService() {
        controlEngine.startService();
        return Message.result(true);
    }

    @PutMapping(path = "/v1/system/service/stop")
    public Message stopService() {
        controlEngine.stopService();
        return Message.result(true);
    }

    @GetMapping(path = "/v1/system/config")
    public Message getSystemConfig() {
        SystemConfigResponse response = new SystemConfigResponse(controlEngine.getCOMPUTE_THREAD(),
                controlEngine.getWALLET_ADDRESS(), controlEngine.getSTOCK_CODE(), controlEngine.getCHEER_WORD());
        return Message.result(response);
    }

    @PutMapping(path = "/v1/system/config")
    public Message updateSystemConfig(@RequestBody SystemConfigRequest request) {
        controlEngine.updateConfig(request.getComputeThread(), request.getWalletAddress(),
                request.getStockCode(), request.getCheerWord());
        return Message.result(true);
    }
}
