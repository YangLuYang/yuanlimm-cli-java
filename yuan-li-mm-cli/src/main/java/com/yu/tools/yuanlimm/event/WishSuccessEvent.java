package com.yu.tools.yuanlimm.event;

import com.yu.tools.yuanlimm.dto.extra.WishResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event - 许愿成功事件
 */
public class WishSuccessEvent extends ApplicationEvent {

    /**
     * 许愿响应
     */
    @Getter
    private WishResponse wishResponse;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public WishSuccessEvent(Object source, WishResponse wishResponse) {
        super(source);
        this.wishResponse = wishResponse;
    }
}
