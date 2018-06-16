package com.yu.tools.yuanlimm.listener;

import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener - 容器信息工具
 */
@SuppressWarnings("WeakerAccess")
@Component
public class ContainerInitializedListener {

    /**
     * Web容器初始化监听
     */
    @EventListener
    public void ContainerInitializedEventListener(EmbeddedServletContainerInitializedEvent event) {
        Integer port = event.getEmbeddedServletContainer().getPort();
        if (port != -1) {
            System.out.println("You can use the following url to manage the system. \nhttp://localhost:" + port + "\n");
        }
    }
}
