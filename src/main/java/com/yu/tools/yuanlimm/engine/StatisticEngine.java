package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.dto.Stock;
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
@SuppressWarnings("WeakerAccess")
@Slf4j
@Lazy(false)
@Component
public class StatisticEngine {
    /**
     * 未知股票
     */
    @Getter
    private final Map<String, AtomicLong> unknownWishStock = new HashMap<>();
    /**
     * 许愿股票
     */
    @Getter
    private Map<Stock, AtomicLong> wishStock = new HashMap<>();
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
            Stock stock = controlEngine.getStockByCode(stockCode);
            if (stock != null) {
                AtomicLong atomicLong = wishStock.get(stock);

                if (atomicLong == null) {
                    atomicLong = new AtomicLong(0);
                    wishStock.put(stock, atomicLong);
                }
                atomicLong.addAndGet(amount);
            } else {
                AtomicLong atomicLong = unknownWishStock.get(stockCode);

                if (atomicLong == null) {
                    atomicLong = new AtomicLong(0);
                    synchronized (unknownWishStock) {
                        unknownWishStock.put(stockCode, atomicLong);
                    }
                }
                atomicLong.addAndGet(amount);
            }
            wishStockAmount.addAndGet(amount);
        } else if (type.equals(WishAwardType.coin)) {
            wishCoinAmount.addAndGet(amount);
        }
    }

    /**
     * 处理未知股票
     */
    @Scheduled(cron = "10/20 * * * * *")
    public void processUnknownWishStock() {
        if (unknownWishStock.size() == 0) {
            return;
        }

        synchronized (unknownWishStock) {
            controlEngine.refreshStockList();
            unknownWishStock.forEach((key, value) -> wishStock.put(controlEngine.getStockByCode(key), value));
            unknownWishStock.clear();
        }
    }
}
