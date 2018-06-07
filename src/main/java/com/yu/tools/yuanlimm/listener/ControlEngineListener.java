package com.yu.tools.yuanlimm.listener;

import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 控制引擎监听
 */
@Component
public class ControlEngineListener {

    /**
     * 控制引擎
     */
    @Resource
    private ControlEngine controlEngine;

    /**
     * 系统就绪事件监听
     */
    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void SystemStartFinishEventListener() {
        controlEngine.init();
    }
}
