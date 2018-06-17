package com.yu.tools.yuanlimm.domain;

import com.yu.tools.yuanlimm.dto.StatisticInfoResponse;
import com.yu.tools.yuanlimm.dto.Stock;
import com.yu.tools.yuanlimm.dto.WishStockInfo;
import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.engine.StatisticEngine;
import com.yu.tools.yuanlimm.model.Message;
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

    @Resource
    private ControlEngine controlEngine;

    @Resource
    private StatisticEngine statisticEngine;

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
