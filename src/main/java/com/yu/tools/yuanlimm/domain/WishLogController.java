package com.yu.tools.yuanlimm.domain;

import com.yu.tools.yuanlimm.engine.WishLogEngine;
import com.yu.tools.yuanlimm.model.Message;
import com.yu.tools.yuanlimm.model.WishLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * RestController - Stock
 */
@RestController
@RequestMapping("/api/")
public class WishLogController {

    @Resource
    private WishLogEngine wishLogEngine;

    @GetMapping(path = "/v1/wishLog/list")
    public Message getWishLogList() {
        ArrayBlockingQueue<WishLog> wishLogQueue = wishLogEngine.getWishLogQueue();
        return Message.result(new ArrayList<>(wishLogQueue));
    }
}
