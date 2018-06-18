package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.enums.WishAwardType;
import com.yu.tools.yuanlimm.model.WishLog;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * Engine - 许愿日志
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
@Slf4j
@Lazy(false)
@Component
public class WishLogEngine {
    /**
     * 队列容量
     */
    private Integer QUEUE_SIZE = 200;
    /**
     * 许愿日志队列
     */
    @Getter
    private ArrayList<WishLog> wishLogList = new ArrayList<>();
    /**
     * 控制引擎
     */
    @Resource
    private ControlEngine controlEngine;

    /**
     * 记录许愿
     */
    public void record(WishAwardType type, Long amount, String stockCode) {
        if (wishLogList.size() >= QUEUE_SIZE) {
            wishLogList.remove(0);
        }

        wishLogList.add(new WishLog(type, amount, stockCode, new Date()));
    }

    /**
     * 处理未知股票
     */
    @Scheduled(cron = "0/30 * * * * *")
    public void processUnknownWishStock() {
        wishLogList.stream()
                .map(WishLog::getStockCode)
                .filter(code -> controlEngine.getStockByCode(code) == null)
                .findAny()
                .ifPresent(code -> controlEngine.refreshStockList());
    }
}
