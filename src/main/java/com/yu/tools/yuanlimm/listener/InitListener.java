package com.yu.tools.yuanlimm.listener;

import com.yu.tools.yuanlimm.event.ApplicationReadyEvent;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

/**
 * Listener - 初始化
 */
@Log
@Component("initListener")
public class InitListener implements ServletContextAware, ApplicationListener<ContextRefreshedEvent> {

    /**
     * ServletContext
     */
    private ServletContext servletContext;

    @Value("${platform.system.version:1}")
    private String systemVersion;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 设置ServletContext
     *
     * @param servletContext ServletContext
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 事件执行
     *
     * @param contextRefreshedEvent ContextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (servletContext != null && contextRefreshedEvent.getApplicationContext().getParent() == null) {
            System.out.println("System Started.");
            applicationContext.publishEvent(new ApplicationReadyEvent(this));
        }
    }
}