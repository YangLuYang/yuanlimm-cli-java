package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.dto.Stock;
import com.yu.tools.yuanlimm.enums.WishAwardType;
import com.yu.tools.yuanlimm.model.WishLog;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Engine - 许愿日志
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
@Slf4j
@Lazy(false)
@Component
public class WishLogEngine {
    /**
     * 未知股票
     */
    @Getter
    private final Map<String, WishLog> unknownWishStock = new HashMap<>();
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
        Stock stock = controlEngine.getStockByCode(stockCode);
        if (stock != null) {
            wishLogList.add(new WishLog(type, amount, stock, new Date()));
        } else {
            synchronized (unknownWishStock) {
                unknownWishStock.put(stockCode, new WishLog(type, amount, null, new Date()));
            }
        }
    }

    /**
     * 处理未知股票
     */
    @Scheduled(cron = "0/20 * * * * *")
    public void processUnknownWishStock() {
        if (unknownWishStock.size() == 0) {
            return;
        }

        synchronized (unknownWishStock) {
            controlEngine.refreshStockList();
            unknownWishStock.forEach((key, value) -> {
                Stock stock = controlEngine.getStockByCode(key);
                wishLogList.add(new WishLog(value.getType(), value.getAmount(), stock, new Date()));
            });

            wishLogList.sort(Comparator.comparing(WishLog::getDate));

            unknownWishStock.clear();
        }
    }
}
