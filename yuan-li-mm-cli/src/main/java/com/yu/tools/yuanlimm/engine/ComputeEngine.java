package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.dto.extra.WishRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Engine - 计算引擎
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@Slf4j
@Lazy(false)
@Component
public class ComputeEngine {
    /**
     * 股票代码字节码
     */
    private byte[] stockCodeBytes;
    /**
     * CheerWord字节码
     */
    private byte[] cheerWordBytes;
    /**
     * 钱包地址字节码
     */
    private byte[] walletAddressBytes;
    /**
     * 控制引擎
     */
    @Resource(name = "controlEngine")
    private ControlEngine controlEngine;
    /**
     * 请求引擎
     */
    @Resource(name = "requestEngine")
    private RequestEngine requestEngine;
    /**
     * 时间引擎
     */
    @Resource(name = "timeEngine")
    private TimeEngine timeEngine;
    /**
     * 计算线程列表
     */
    private List<Thread> threadList = new LinkedList<>();
    /**
     * 已计算的HASH计数
     */
    @Getter
    private AtomicLong hashCounter = new AtomicLong();
    /**
     * 随机函数
     */
    private Random random = new Random();

    /**
     * 多线程任务
     */
    private Runnable task = () -> {
        log.debug("ComputeEngine Thread Started.");
        //noinspection InfiniteLoopStatement
        while (!Thread.interrupted()) {
            while (requestEngine.isRequestOverLimit()) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < 1000; i++) {
                execute();
            }
            hashCounter.addAndGet(1000);
        }
    };

    /**
     * 开始
     */
    public void start() {
        this.stockCodeBytes = controlEngine.getSTOCK_CODE().getBytes();
        this.walletAddressBytes = controlEngine.getWALLET_ADDRESS().getBytes();
        this.cheerWordBytes = controlEngine.getCHEER_WORD().getBytes(Charset.forName("utf-8"));

        for (int i = 0; i < controlEngine.getCOMPUTE_THREAD(); i++) {
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
     * 执行计算
     */
    private void execute() {
        int hard = controlEngine.getHASH_HARD();

        Long lovePower = (Math.abs(random.nextLong()) % 99999999999L);
        byte[] cheerWord = this.cheerWordBytes;
        byte[] address = this.walletAddressBytes;
        byte[] lp = lovePower.toString().getBytes();
        byte[] unixTime = timeEngine.getCurrentUnixMinuteBytes();
        byte[] stockCode = this.stockCodeBytes;

        byte[] mix = new byte[cheerWord.length + address.length + lp.length + unixTime.length + stockCode.length];

        System.arraycopy(cheerWord, 0, mix, 0, cheerWord.length);
        System.arraycopy(address, 0, mix, cheerWord.length, address.length);
        System.arraycopy(lp, 0, mix, cheerWord.length + address.length, lp.length);
        System.arraycopy(unixTime, 0, mix, cheerWord.length + address.length + lp.length, unixTime.length);
        System.arraycopy(stockCode, 0, mix, cheerWord.length + address.length + lp.length + unixTime.length, stockCode.length);

        byte[] bytes;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(mix);
            bytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }

        int zero = hard / 8;

        for (int index = 1; index <= zero; index++) {
            if (bytes[bytes.length - index] != 0) {
                return;
            }
        }

        int left = hard % 8;

        if (left > 0) {
            if ((byte) (bytes[bytes.length - zero - 1] << (8 - left)) != 0) {
                return;
            }
        }

        WishRequest request = new WishRequest(controlEngine.getCHEER_WORD(), controlEngine.getWALLET_ADDRESS(), lovePower, controlEngine.getSTOCK_CODE());
        requestEngine.add(request);
    }
}
