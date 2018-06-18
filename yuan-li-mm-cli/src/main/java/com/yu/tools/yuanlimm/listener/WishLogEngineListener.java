package com.yu.tools.yuanlimm.listener;

import com.yu.tools.yuanlimm.config.WebSocketRouter;
import com.yu.tools.yuanlimm.dto.api.WishLogInfo;
import com.yu.tools.yuanlimm.dto.extra.WishResponse;
import com.yu.tools.yuanlimm.engine.ClusterWorkerEngine;
import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.engine.WishLogEngine;
import com.yu.tools.yuanlimm.enums.SystemMode;
import com.yu.tools.yuanlimm.enums.WebSocketMessageType;
import com.yu.tools.yuanlimm.event.WishSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

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
            WishLogInfo info = new WishLogInfo(response.getType(), response.getAmount(), response.getStock(), new Date());
            clusterWorkerEngine.send(WebSocketRouter.SEND_WORKER_WISH_LOG, WebSocketMessageType.statistic, info);
        }
        wishLogEngine.record(response.getType(), response.getAmount(), response.getStock());
    }
}
