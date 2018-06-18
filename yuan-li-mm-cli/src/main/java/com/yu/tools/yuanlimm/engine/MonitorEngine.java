package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.config.WebSocketRouter;
import com.yu.tools.yuanlimm.dto.ws.WorkComputeInfo;
import com.yu.tools.yuanlimm.enums.SystemMode;
import com.yu.tools.yuanlimm.enums.SystemStatus;
import com.yu.tools.yuanlimm.enums.WebSocketMessageType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Engine - 监控引擎
 */
@Slf4j
@Lazy(false)
@Component
public class MonitorEngine {
    /**
     * 计算引擎
     */
    @Resource
    private ComputeEngine computeEngine;
    /**
     * 请求引擎
     */
    @Resource
    private RequestEngine requestEngine;
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
     * 上次Hash速度
     */
    @Getter
    private Long lastHashSpeed = 0L;
    /**
     * 上次Hash计数
     */
    private Long lastHashCount = 0L;
    /**
     * 上次请求成功计数
     */
    private Long lastRequestSuccessCount = 0L;
    /**
     * 总援力
     */
    @Getter
    private AtomicLong totalCoin = new AtomicLong(0);
    /**
     * 总股票
     */
    @Getter
    private AtomicLong totalStock = new AtomicLong(0);

    /**
     * 统计
     */
    @Scheduled(fixedDelay = 1000L)
    public void output() {
        if (controlEngine.getSYSTEM_STATUS().equals(SystemStatus.stopped)
                || controlEngine.getSYSTEM_STATUS().equals(SystemStatus.needConfig)) {
            return;
        }

        Long currentHashCount = computeEngine.getHashCounter().get();
        Long currentRequestSuccessCount = requestEngine.getRequestSuccessCounter().get();

        String template = "Hash: %.2fM "
                + "\tReq: %d "
                + "\tHard: %d "
                + "\tCoin: %.2f "
                + "\tStock: %d";

        if (requestEngine.isRequestOverLimit()) {
            template += "\t [SLEEP]";
        }

        System.out.println(String.format(template,
                (currentHashCount - lastHashCount) / 1000000.00,
                (currentRequestSuccessCount - lastRequestSuccessCount),
                controlEngine.getHASH_HARD(),
                totalCoin.get() / 100.0,
                totalStock.get()));

        lastHashSpeed = (currentHashCount - lastHashCount);
        lastHashCount = currentHashCount;
        lastRequestSuccessCount = currentRequestSuccessCount;

        if (controlEngine.getSYSTEM_MODE().equals(SystemMode.worker)) {
            WorkComputeInfo info = new WorkComputeInfo(lastHashSpeed);
            clusterWorkerEngine.send(WebSocketRouter.SEND_WORKER_COMPUTE, WebSocketMessageType.statisticInfo, info);
        }
    }
}
