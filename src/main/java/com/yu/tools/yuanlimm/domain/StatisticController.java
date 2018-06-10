package com.yu.tools.yuanlimm.domain;

import com.yu.tools.yuanlimm.dto.StatisticInfoResponse;
import com.yu.tools.yuanlimm.dto.Stock;
import com.yu.tools.yuanlimm.dto.WishStockInfo;
import com.yu.tools.yuanlimm.engine.StatisticEngine;
import com.yu.tools.yuanlimm.model.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * RestController - Statistic
 */
@RestController
@RequestMapping("/api/")
public class StatisticController {

    @Resource
    private StatisticEngine statisticEngine;

    @GetMapping(path = "/v1/statistic")
    public Message getStatisticInfo() {
        AtomicLong wishCoinAmount = statisticEngine.getWishCoinAmount();
        AtomicLong wishStockAmount = statisticEngine.getWishStockAmount();
        Map<Stock, AtomicLong> wishStock = statisticEngine.getWishStock();

        List<WishStockInfo> stockInfoList = wishStock.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().get()))
                .map(entry -> new WishStockInfo(entry.getKey(), entry.getValue().get()))
                .collect(Collectors.toList());

        return Message.result(new StatisticInfoResponse(stockInfoList, wishCoinAmount.get(), wishStockAmount.get()));
    }
}
