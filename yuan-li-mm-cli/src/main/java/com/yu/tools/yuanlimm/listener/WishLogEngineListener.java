package com.yu.tools.yuanlimm.listener;

import com.yu.tools.yuanlimm.dto.extra.WishResponse;
import com.yu.tools.yuanlimm.engine.WishLogEngine;
import com.yu.tools.yuanlimm.event.WishSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 许愿日志引擎监听
 */
@Component
public class WishLogEngineListener {

    /**
     * 许愿日志引擎
     */
    @Resource
    private WishLogEngine wishLogEngine;

    /**
     * 许愿成功事件监听
     */
    @Async
    @EventListener(WishSuccessEvent.class)
    public void WishSuccessEventListener(WishSuccessEvent event) {
        WishResponse response = event.getWishResponse();
        wishLogEngine.record(response.getType(), response.getAmount(), response.getStock());
    }
}
