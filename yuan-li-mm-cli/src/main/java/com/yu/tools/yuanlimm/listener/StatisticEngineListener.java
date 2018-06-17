package com.yu.tools.yuanlimm.listener;

import com.yu.tools.yuanlimm.config.WebSocketRouter;
import com.yu.tools.yuanlimm.dto.WishResponse;
import com.yu.tools.yuanlimm.dto.WishResultInfo;
import com.yu.tools.yuanlimm.engine.ClusterWorkerEngine;
import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.engine.StatisticEngine;
import com.yu.tools.yuanlimm.enums.SystemMode;
import com.yu.tools.yuanlimm.enums.WebSocketMessageType;
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
     * 控制引擎
     */
    @Resource
    private ControlEngine controlEngine;
    /**
     * 集群Worker引擎
     */
    @Resource
    private ClusterWorkerEngine clusterWorkerEngine;

    /**
     * 许愿成功事件监听
     */
    @Async
    @EventListener(WishSuccessEvent.class)
    public void WishSuccessEventListener(WishSuccessEvent event) {
        WishResponse response = event.getWishResponse();

        if (controlEngine.getSYSTEM_MODE().equals(SystemMode.worker)) {
            WishResultInfo info = new WishResultInfo(response.getType(), response.getAmount(), response.getStock());
            clusterWorkerEngine.send(WebSocketRouter.SEND_WORKER_WISH_RESULT, WebSocketMessageType.statistic, info);
        }
        statisticEngine.record(response.getType(), response.getAmount(), response.getStock());
    }
}
