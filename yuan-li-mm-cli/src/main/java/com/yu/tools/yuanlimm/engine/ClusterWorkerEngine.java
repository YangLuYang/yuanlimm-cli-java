package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.config.WebSocketRouter;
import com.yu.tools.yuanlimm.dto.CommonWebSocketMessage;
import com.yu.tools.yuanlimm.dto.Stock;
import com.yu.tools.yuanlimm.dto.WorkStatisticInfo;
import com.yu.tools.yuanlimm.dto.WorkerMonitorInfo;
import com.yu.tools.yuanlimm.enums.WebSocketMessageType;
import com.yu.tools.yuanlimm.handler.CustomStompSessionHandler;
import com.yu.tools.yuanlimm.model.PackedWebSocketMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Engine - 集群管理引擎
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "WeakerAccess", "Duplicates"})
@Slf4j
@Lazy(false)
@Component
public class ClusterWorkerEngine {
    /**
     * 控制引擎
     */
    @Resource
    private ControlEngine controlEngine;
    /**
     * 监控引擎
     */
    @Resource
    private MonitorEngine monitorEngine;
    /**
     * 计算引擎
     */
    @Resource
    private ComputeEngine computeEngine;
    /**
     * 统计引擎
     */
    @Resource
    private StatisticEngine statisticEngine;
    /**
     * Stomp消息处理器
     */
    @Resource
    private CustomStompSessionHandler customStompSessionHandler;
    /**
     * WebSocket消息队列
     */
    private LinkedBlockingQueue<PackedWebSocketMessage> webSocketMessageQueue = new LinkedBlockingQueue<>();
    /**
     * WebSocket会话
     */
    @Getter
    @Setter
    private StompSession session;

    /**
     * 初始化Worker
     */
    public void init() {
        String url = "ws://localhost:8080/portfolio";

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new StringMessageConverter());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompHeaders headers = new StompHeaders();
        headers.add("login", controlEngine.getWORKER_NAME());

        System.out.println("正在连接...");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("连接超时，程序即将退出");
                System.exit(1);
            }
        }, 5000L);

        ListenableFuture<StompSession> connect = stompClient.connect(url, (WebSocketHttpHeaders) null, headers, customStompSessionHandler);
        connect.addCallback(new ListenableFutureCallback<StompSession>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("连接失败，程序即将退出");
                System.exit(0);
            }

            @Override
            public void onSuccess(StompSession stompSession) {
                timer.cancel();
                System.out.println("连接成功");
            }
        });

        new Thread(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    PackedWebSocketMessage message = webSocketMessageQueue.take();
                    send(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 发送消息
     *
     * @param destination 目标
     * @param type        消息类型
     * @param message     消息
     */
    public <T> void send(String destination, WebSocketMessageType type, T message) {
        try {
            webSocketMessageQueue.put(new PackedWebSocketMessage<>(destination, new CommonWebSocketMessage<>(type, message)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void send(PackedWebSocketMessage message) {
        session.send(message.getDestination(), message.getMessage());
    }

    /**
     * 订阅
     *
     * @param destination 目标
     * @param type        消息类型
     * @param consumer    消费者
     * @param <T>         消息类型
     */
    public <T> void subscribe(String destination, Class<T> type, Consumer<T> consumer) {
        session.subscribe(destination, new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                //noinspection unchecked
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * 开始发送状态
     */
    public void startSendStatus() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                send(WebSocketRouter.SEND_WORKER_STATUS, WebSocketMessageType.status, controlEngine.getSYSTEM_STATUS());
            }
        }, 1000, 1000);
    }

    /**
     * 开始发送监控
     */
    public void startSendMonitor() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Long hashSpeed = monitorEngine.getLastHashSpeed();
                Long totalHash = computeEngine.getHashCounter().get();
                Integer hard = controlEngine.getHASH_HARD();
                WorkerMonitorInfo info = new WorkerMonitorInfo(hashSpeed, hard, totalHash, new Date());

                send(WebSocketRouter.SEND_WORKER_MONITOR, WebSocketMessageType.monitor, info);
            }
        }, 1000, 1000);
    }

    /**
     * 开始发送统计信息
     */
    @SuppressWarnings("unused")
    public void startSendStatistic() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                AtomicLong wishCoinAmount = statisticEngine.getWishCoinAmount();
                AtomicLong wishStockAmount = statisticEngine.getWishStockAmount();
                Map<String, AtomicLong> wishStock = statisticEngine.getWishStock();

                List<WorkStatisticInfo.WishStockInfo> stockInfoList = wishStock.entrySet().stream()
                        .sorted(Comparator.comparing(entry -> entry.getValue().get()))
                        .map(entry -> {
                            Stock stock = controlEngine.getStockByCode(entry.getKey());
                            if (stock == null) {
                                return null;
                            }
                            return new WorkStatisticInfo.WishStockInfo(stock, entry.getValue().get());
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                WorkStatisticInfo info = new WorkStatisticInfo(stockInfoList, wishCoinAmount.get(), wishStockAmount.get());

                send(WebSocketRouter.SEND_WORKER_STATISTIC, WebSocketMessageType.statistic, info);
            }
        }, 1000, 1000);
    }

    /**
     * 开始监听配置
     */
    public void startListenConfig() {
        this.subscribe(WebSocketRouter.RECEIVE_WORKER_CONFIG, String.class, System.out::println);
    }
}
