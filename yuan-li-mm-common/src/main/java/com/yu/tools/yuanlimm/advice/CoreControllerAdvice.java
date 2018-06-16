package com.yu.tools.yuanlimm.advice;

import com.yu.tools.yuanlimm.enums.MessageStatus;
import com.yu.tools.yuanlimm.exception.BusinessException;
import com.yu.tools.yuanlimm.model.Message;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Priority;

/**
 * ExceptionAdvice - 异常拦截处理
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@ControllerAdvice(annotations = RestController.class)
@Priority(Ordered.LOWEST_PRECEDENCE - 1)
public class CoreControllerAdvice extends BaseControllerAdvice {

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Message businessExceptionHandler(BusinessException e) {
        return getErrorMessage(MessageStatus.error, e.getMessage(), null);
    }
}