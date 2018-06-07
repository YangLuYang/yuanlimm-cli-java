package com.yu.tools.yuanlimm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 援力满满许愿机
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class YuanLiMmApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuanLiMmApplication.class, args);
    }
}
