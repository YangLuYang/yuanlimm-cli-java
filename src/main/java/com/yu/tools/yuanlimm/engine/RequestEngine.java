package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.dto.WishRequest;
import com.yu.tools.yuanlimm.dto.WishResponse;
import com.yu.tools.yuanlimm.event.WishSuccessEvent;
import com.yu.tools.yuanlimm.util.MinuteCounter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Engine - 请求引擎
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@Slf4j
@Component
public class RequestEngine {
    /**
     * 每分钟请求限制
     */
    public static final Long requestPerMinuteLimit = 500L;
    /**
     * 请求队列深度
     */
    private static final Integer QUEUE_SIZE = 8;
    /**
     * 每分钟请求成功计数
     */
    @Getter
    private MinuteCounter requestSuccessMinuteCounter = new MinuteCounter();
    /**
     * Rest模板
     */
    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;
    /**
     * 监控引擎
     */
    @Resource
    private MonitorEngine monitorEngine;
    /**
     * 控制引擎
     */
    @Resource
    private ControlEngine controlEngine;
    /**
     * 许愿日志引擎
     */
    @Resource
    private WishLogEngine wishLogEngine;
    /**
     * 统计引擎
     */
    @Resource
    private StatisticEngine statisticEngine;
    /**
     * 计算线程列表
     */
    private List<Thread> threadList = new LinkedList<>();
    /**
     * 请求计数器
     */
    @Getter
    private AtomicLong requestCounter = new AtomicLong();
    /**
     * 请求成功计数器
     */
    @Getter
    private AtomicLong requestSuccessCounter = new AtomicLong();
    /**
     * 请求队列
     */
    @Getter
    private LinkedBlockingQueue<WishRequest> queue = new LinkedBlockingQueue<>(QUEUE_SIZE);
    /**
     * ApplicationContext
     */
    @Resource
    private ApplicationContext applicationContext;

    /**
     * 请求任务
     */
    private Runnable task = () -> {
        log.debug("RequestEngine Thread Started.");
        //noinspection InfiniteLoopStatement
        while (!Thread.interrupted()) {
            WishRequest request;
            try {
                request = queue.take();
            } catch (InterruptedException e) {
                continue;
            }

            HttpHeaders requestHeaders = new HttpHeaders();
            HttpEntity<WishRequest> requestEntity = new HttpEntity<>(request, requestHeaders);

            try {
                requestCounter.getAndIncrement();

                WishResponse response = restTemplate.postForObject("https://www.yuanlimm.com/api/super_wishs",
                        requestEntity, WishResponse.class);

                if (response.getSuccess() == null) {
                    log.error("ERROR:\t" + response.getMsg());
                    System.exit(0);
                }

                if (response.getHard() != null && !controlEngine.getHASH_HARD().equals(response.getHard())) {
                    controlEngine.setHASH_HARD(response.getHard());
                    queue.clear();
                }

                if (response.getSuccess()) {
                    requestSuccessCounter.incrementAndGet();
                    requestSuccessMinuteCounter.increment();

                    applicationContext.publishEvent(new WishSuccessEvent(this, response));

                    switch (response.getType()) {
                        case coin:
                            monitorEngine.getTotalCoin().getAndAdd(response.getAmount());
                            break;
                        case stock:
                            monitorEngine.getTotalStock().getAndAdd(response.getAmount());
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                } else {
                    log.warn("RequestFail:\t" + response);
                }
            } catch (HttpClientErrorException e) {
                if (e.getMessage().contains("429")) {
                    requestSuccessMinuteCounter.add(requestPerMinuteLimit);
                    log.warn("请求服务器频率超过限制", e);
                } else if (e.getMessage().contains("503")) {
                    log.warn("请求服务器失败", e);
                } else {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 开始
     */
    public void start() {
        for (int i = 0; i < controlEngine.getREQUEST_THREAD(); i++) {
            Thread thread = new Thread(task);
            thread.start();
            threadList.add(thread);
        }
    }

    /**
     * 停止
     */
    public void stop() {
        threadList.forEach(Thread::interrupt);
        threadList.clear();
    }

    /**
     * 添加
     *
     * @param request 请求
     */
    public void add(WishRequest request) {
        try {
            queue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求是否超过限制
     *
     * @return 是否超过限制
     */
    public Boolean isRequestOverLimit() {
        return requestSuccessMinuteCounter.get() >= RequestEngine.requestPerMinuteLimit;
    }
}
