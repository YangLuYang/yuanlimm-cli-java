package com.yu.tools.yuanlimm.domain.api;

import com.yu.tools.yuanlimm.dto.api.StatisticInfoResponse;
import com.yu.tools.yuanlimm.dto.api.WishStockInfo;
import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.engine.StatisticEngine;
import com.yu.tools.yuanlimm.model.Message;
import com.yu.tools.yuanlimm.model.Stock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * RestController - Statistic
 */
@SuppressWarnings("Duplicates")
@RestController
@RequestMapping("/api/")
public class StatisticController {
    /**
     * 控制引擎
     */
    @Resource
    private ControlEngine controlEngine;
    /**
     * 统计引擎
     */
    @Resource
    private StatisticEngine statisticEngine;

    /**
     * 获取统计信息
     *
     * @return 消息
     */
    @GetMapping(path = "/v1/statistic")
    public Message getStatisticInfo() {
        AtomicLong wishCoinAmount = statisticEngine.getWishCoinAmount();
        AtomicLong wishStockAmount = statisticEngine.getWishStockAmount();
        Map<String, AtomicLong> wishStock = statisticEngine.getWishStock();

        List<WishStockInfo> stockInfoList = wishStock.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().get()))
                .map(entry -> {
                    Stock stock = controlEngine.getStockByCode(entry.getKey());
                    if (stock == null) {
                        return null;
                    }
                    return new WishStockInfo(stock, entry.getValue().get());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Message.result(new StatisticInfoResponse(stockInfoList, wishCoinAmount.get(), wishStockAmount.get()));
    }
}
