package com.yu.tools.yuanlimm.domain;

import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.model.Message;
import com.yu.tools.yuanlimm.model.Stock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * RestController - Stock
 */
@RestController
@RequestMapping("/api/")
public class StockController {

    @Resource
    private ControlEngine controlEngine;

    @GetMapping(path = "/v1/stock/list")
    public Message getStockList() {
        List<Stock> stockList = controlEngine.getStockList();
        return Message.result(stockList);
    }
}
