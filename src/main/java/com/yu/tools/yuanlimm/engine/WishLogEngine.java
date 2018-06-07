package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.dto.Stock;
import com.yu.tools.yuanlimm.enums.WishAwardType;
import com.yu.tools.yuanlimm.model.WishLog;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Engine - 许愿日志
 */
@SuppressWarnings("WeakerAccess")
@Slf4j
@Lazy(false)
@Component
public class WishLogEngine {

    /**
     * 队列容量
     */
    private Integer QUEUE_SIZE = 100;

    /**
     * 许愿日志队列
     */
    @Getter
    private ArrayBlockingQueue<WishLog> wishLogQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);

    /**
     * 记录许愿
     */
    public void recordWish(WishAwardType type, Long amount, Stock stock) {
        if (wishLogQueue.remainingCapacity() == 0) {
            wishLogQueue.remove();
        }
        wishLogQueue.add(new WishLog(type, amount, stock, new Date()));
    }
}
