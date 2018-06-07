package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.util.TimeUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Engine - 时间引擎
 */
@Slf4j
@Lazy(false)
@Component
public class TimeEngine {

    /**
     * 当前Unix时间戳字节
     */
    @Getter
    private byte[] currentUnixMinuteBytes = new byte[0];

    /**
     * 更新
     */
    @Scheduled(fixedDelay = 1000L)
    public void update() {
        this.currentUnixMinuteBytes = TimeUtil.getUnixMinute().toString().getBytes();
    }
}
