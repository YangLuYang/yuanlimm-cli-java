package com.yu.tools.yuanlimm.advice;

import com.yu.tools.yuanlimm.enums.MessageStatus;
import com.yu.tools.yuanlimm.model.Message;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Priority;

/**
 * ExceptionAdvice - 通用异常拦截处理
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@ControllerAdvice(annotations = RestController.class)
@Priority(Ordered.LOWEST_PRECEDENCE)
public class CommonControllerAdvice extends BaseControllerAdvice {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Message commonExceptionHandle(Exception e) {
        e.printStackTrace();
        return getErrorMessage(MessageStatus.error, e.getMessage(), null);
    }
}