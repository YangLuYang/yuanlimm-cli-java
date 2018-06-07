package com.yu.tools.yuanlimm.model;

import com.yu.tools.yuanlimm.enums.MessageStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 消息
 */
@Getter
@Setter
@SuppressWarnings({"unused", "WeakerAccess"})
public class Message implements Serializable {

    /**
     * 类型
     */
    private MessageStatus status;

    /**
     * 数据
     */
    private Object data;

    /**
     * 内容
     */
    private String message;

    /**
     * 构造方法
     */
    public Message() {
    }

    /**
     * 构造方法
     *
     * @param status  类型
     * @param message 内容
     */
    public Message(MessageStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * 构造方法
     *
     * @param status  类型
     * @param data    数据包
     * @param message 内容
     */
    public Message(MessageStatus status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    /**
     * 返回成功消息
     *
     * @param data 数据
     * @return 成功消息
     */
    public static Message result(List<?> data) {
        Message message = new Message();
        if (data == null) {
            message.setStatus(MessageStatus.error);
            message.setMessage("数据获取失败");
        } else if (data.size() > 0) {
            message.setStatus(MessageStatus.success);
            message.setData(data);
        } else {
            message.setStatus(MessageStatus.noData);
            message.setMessage("无数据");
        }
        return message;
    }

    /**
     * 返回操作结果
     *
     * @param result 操作结果
     * @return 操作结果
     */
    public static Message result(boolean result) {
        Message message = new Message();
        message.setStatus(result ? MessageStatus.success : MessageStatus.error);
        message.setMessage(result ? "操作成功" : "操作失败");
        return message;
    }

    /**
     * 返回操作结果
     *
     * @param result      操作结果
     * @param textMessage 文本消息
     * @return 操作结果
     */
    public static Message result(boolean result, String textMessage) {
        Message message = new Message();
        message.setStatus(result ? MessageStatus.success : MessageStatus.error);
        message.setMessage(textMessage);
        return message;
    }

    /**
     * 返回对象
     *
     * @return 对象
     */
    public static Message result(Object object) {
        Message message = new Message();
        message.setStatus(object != null ? MessageStatus.success : MessageStatus.noData);
        message.setData(object);
        return message;
    }

    /**
     * 返回对象
     *
     * @return 对象
     */
    public static Message result(Object object, String textMessage) {
        Message message = new Message();
        message.setStatus(object != null ? MessageStatus.success : MessageStatus.noData);
        message.setData(object);
        message.setMessage(textMessage);
        return message;
    }
}