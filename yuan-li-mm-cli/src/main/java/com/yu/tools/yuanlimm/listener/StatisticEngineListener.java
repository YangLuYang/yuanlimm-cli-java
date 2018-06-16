package com.yu.tools.yuanlimm.listener;

import com.yu.tools.yuanlimm.dto.WishResponse;
import com.yu.tools.yuanlimm.engine.StatisticEngine;
import com.yu.tools.yuanlimm.event.WishSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 统计引擎监听
 */
@Component
public class StatisticEngineListener {

    /**
     * 统计引擎
     */
    @Resource
    private StatisticEngine statisticEngine;

    /**
     * 许愿成功事件监听
     */
    @Async
    @EventListener(WishSuccessEvent.class)
    public void WishSuccessEventListener(WishSuccessEvent event) {
        WishResponse response = event.getWishResponse();
        statisticEngine.record(response.getType(), response.getAmount(), response.getStock());
    }
}
