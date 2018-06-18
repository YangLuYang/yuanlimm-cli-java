package com.yu.tools.yuanlimm.domain;

import com.yu.tools.yuanlimm.dto.api.WishLogInfo;
import com.yu.tools.yuanlimm.engine.ControlEngine;
import com.yu.tools.yuanlimm.engine.WishLogEngine;
import com.yu.tools.yuanlimm.model.Message;
import com.yu.tools.yuanlimm.model.Stock;
import com.yu.tools.yuanlimm.model.WishLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * RestController - Stock
 */
@RestController
@RequestMapping("/api/")
public class WishLogController {

    @Resource
    private ControlEngine controlEngine;

    @Resource
    private WishLogEngine wishLogEngine;

    @GetMapping(path = "/v1/wishLog/list")
    public Message getWishLogList() {
        ArrayList<WishLog> wishLogList = wishLogEngine.getWishLogList();

        List<WishLogInfo> infoList = wishLogList.stream()
                .map(wishLog -> {
                    Stock stock = controlEngine.getStockByCode(wishLog.getStockCode());
                    if (stock == null) {
                        return null;
                    }
                    return new WishLogInfo(wishLog.getType(), wishLog.getAmount(), stock, wishLog.getDate());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return Message.result(infoList);
    }
}
