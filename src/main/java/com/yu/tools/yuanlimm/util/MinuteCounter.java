package com.yu.tools.yuanlimm.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 分钟计数器
 */
public class MinuteCounter {

    /**
     * 上一分钟
     */
    private Long lastMinute;

    /**
     * 分钟计数
     */
    private AtomicLong minuteCounter = new AtomicLong();

    /**
     * 增长
     */
    public void increment() {
        checkData();
        minuteCounter.incrementAndGet();
    }

    /**
     * 增加
     */
    public void add(Long number) {
        checkData();
        minuteCounter.getAndAdd(number);
    }

    /**
     * 获取
     *
     * @return 分钟计数
     */
    public Long get() {
        checkData();
        return minuteCounter.get();
    }

    /**
     * 校验数据
     */
    private void checkData() {
        if (lastMinute == null) {
            lastMinute = TimeUtil.getUnixMinute();
        } else if (!lastMinute.equals(TimeUtil.getUnixMinute())) {
            lastMinute = TimeUtil.getUnixMinute();
            minuteCounter.set(0L);
        }
    }
}
