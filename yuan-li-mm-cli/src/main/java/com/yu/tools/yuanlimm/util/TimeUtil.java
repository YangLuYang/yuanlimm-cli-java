package com.yu.tools.yuanlimm.util;

import java.util.Calendar;

/**
 * 时间工具类
 */
@SuppressWarnings("WeakerAccess")
public class TimeUtil {

    /**
     * 获取Unix分钟
     *
     * @return Unix分钟
     */
    public static Long getUnixMinute() {
        Calendar calendar = Calendar.getInstance();
        return (calendar.getTimeInMillis() / 1000) - calendar.get(Calendar.SECOND);
    }
}
