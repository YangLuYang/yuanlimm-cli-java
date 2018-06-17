package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.enums.WishAwardType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Engine - 统计引擎
 */
@SuppressWarnings({"WeakerAccess", "Duplicates"})
@Slf4j
@Lazy(false)
@Component
public class StatisticEngine {
    /**
     * 许愿股票
     */
    @Getter
    private Map<String, AtomicLong> wishStock = new HashMap<>();
    /**
     * 控制引擎
     */
    @Resource
    private ControlEngine controlEngine;
    /**
     * 许愿援力
     */
    @Getter
    private AtomicLong wishCoinAmount = new AtomicLong();
    /**
     * 许愿股票
     */
    @Getter
    private AtomicLong wishStockAmount = new AtomicLong();

    /**
     * 记录许愿
     */
    public void record(WishAwardType type, Long amount, String stockCode) {
        if (type.equals(WishAwardType.stock)) {
            AtomicLong atomicLong = wishStock.get(stockCode);

            if (atomicLong == null) {
                atomicLong = new AtomicLong(0);
                wishStock.put(stockCode, atomicLong);
            }
            atomicLong.addAndGet(amount);

            wishStockAmount.addAndGet(amount);
        } else if (type.equals(WishAwardType.coin)) {
            wishCoinAmount.addAndGet(amount);
        }
    }

    /**
     * 处理未知股票
     */
    @Scheduled(cron = "10/30 * * * * *")
    public void processUnknownWishStock() {
        wishStock.entrySet().stream()
                .map(Map.Entry::getKey)
                .filter(code -> controlEngine.getStockByCode(code) == null)
                .findAny()
                .ifPresent(code -> controlEngine.refreshStockList());
    }
}
