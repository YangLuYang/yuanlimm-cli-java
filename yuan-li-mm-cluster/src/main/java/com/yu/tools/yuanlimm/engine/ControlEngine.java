package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.dto.extra.StocksResponse;
import com.yu.tools.yuanlimm.dto.extra.WishResponse;
import com.yu.tools.yuanlimm.entity.WorkerNode;
import com.yu.tools.yuanlimm.enums.SystemStatus;
import com.yu.tools.yuanlimm.model.Stock;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * Engine - 控制引擎
 */
@SuppressWarnings({"unused", "WeakerAccess", "Duplicates"})
@Slf4j
@Lazy(false)
@Component
public class ControlEngine {
    /**
     * Worker系统状态
     */
    @Getter
    private Map<String, SystemStatus> workerSystemStatus = new HashMap<>();
    /**
     * 股票列表
     */
    private List<Stock> stockList = new ArrayList<>();
    /**
     * 股票Map
     */
    private Map<String, Stock> stockMap = new HashMap<>();
    /**
     * RestTemplate
     */
    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;
    /**
     * HASH难度
     */
    @Getter
    @Setter
    private Integer HASH_HARD = 0;

    /**
     * Init
     */
    public void init() {
        this.refreshStockList();
    }

    /**
     * 更新Worker系统状态
     *
     * @param workerNode   Worker节点
     * @param systemStatus 系统状态
     */
    public void updateWorkerSystemStatus(WorkerNode workerNode, SystemStatus systemStatus) {
        this.workerSystemStatus.put(workerNode.getName(), systemStatus);
    }

    /**
     * Worker断开连接
     *
     * @param workerNode Worker节点
     */
    public void workerDisconnected(WorkerNode workerNode) {
        this.workerSystemStatus.remove(workerNode.getName());
    }

    /**
     * 刷新股票列表
     */
    @Scheduled(cron = "0 0 * * * *")
    public void refreshStockList() {
        String api = "https://www.yuanlimm.com/api/stocks?page=%d";
        Integer pageIndex = 1;

        this.stockList.clear();

        try {
            StocksResponse response;
            do {
                response = restTemplate.getForObject(String.format(api, pageIndex), StocksResponse.class);
                this.stockList.addAll(response.getData());
                pageIndex++;
            } while (response.getData().size() != 0);
        } catch (HttpClientErrorException e) {
            if (e.getMessage().contains("429")) {
                log.info("请求服务器频率超过限制", e);
            } else if (e.getMessage().contains("503")) {
                log.info("请求服务器失败", e);
            } else if (e.getMessage().contains("504")) {
                log.info("请求服务器失败", e);
            } else {
                log.warn("请求服务器失败", e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.stockList.forEach(stock -> stockMap.put(stock.getCode(), stock));
    }

    /**
     * 获取股票列表
     *
     * @return 股票列表
     */
    public List<Stock> getStockList() {
        if (Objects.isNull(this.stockList)) {
            this.refreshStockList();
        }
        return this.stockList;
    }

    /**
     * 根据Code获取股票
     *
     * @param code 股票代码
     * @return 股票
     */
    public Stock getStockByCode(String code) {
        return stockMap.get(code);
    }

    /**
     * 每30秒重置难度
     */
    @Scheduled(fixedDelay = 1000 * 20)
    public void updateHashHard() {
        Optional.ofNullable(this.getHashHard()).ifPresent(number -> this.HASH_HARD = number);
    }

    /**
     * 获取难度
     *
     * @return 难度
     */
    private Integer getHashHard() {
        WishResponse response = restTemplate.getForObject("https://www.yuanlimm.com/api/super_wishs", WishResponse.class);
        if (response.getHard() != null) {
            return response.getHard();
        }
        return null;
    }
}
