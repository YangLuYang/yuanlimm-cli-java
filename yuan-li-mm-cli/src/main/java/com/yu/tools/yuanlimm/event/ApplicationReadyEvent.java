package com.yu.tools.yuanlimm.event;

import org.springframework.context.ApplicationEvent;

/**
 * Event - 系统就绪事件
 */
public class ApplicationReadyEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ApplicationReadyEvent(Object source) {
        super(source);
    }
}
