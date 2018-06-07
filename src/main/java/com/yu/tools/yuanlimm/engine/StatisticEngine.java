package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.dto.Stock;
import com.yu.tools.yuanlimm.enums.WishAwardType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

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
     * 许愿股票
     */
    @Getter
    private Map<Stock, AtomicLong> wishStock = new HashMap<>();
    /**
     * 许愿援力
     */
    @Getter
    private AtomicLong wishCoin = new AtomicLong();

    /**
     * 记录许愿
     */
    public void recordWish(WishAwardType type, Long amount, Stock stock) {
        if (type.equals(WishAwardType.stock)) {
            AtomicLong atomicLong = wishStock.get(stock);
            if (atomicLong == null) {
                atomicLong = new AtomicLong(0);
                wishStock.put(stock, atomicLong);
            }

            atomicLong.addAndGet(amount);
        } else if (type.equals(WishAwardType.coin)) {
            wishCoin.addAndGet(amount);
        }
    }
}
