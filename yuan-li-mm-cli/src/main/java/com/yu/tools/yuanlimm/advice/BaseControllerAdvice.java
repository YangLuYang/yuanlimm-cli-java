package com.yu.tools.yuanlimm.advice;

import com.yu.tools.yuanlimm.enums.MessageStatus;
import com.yu.tools.yuanlimm.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExceptionAdvice - 异常拦截处理
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public abstract class BaseControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(BaseControllerAdvice.class);

    /**
     * 生成错误信息map
     *
     * @return 错误信息map
     */
    protected ErrorMessage getErrorMessage(MessageStatus state, String message, String detail) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(state);
        errorMessage.setMessage(message);
        errorMessage.setDetail(detail);
        errorMessage.setCode("-1");
        return errorMessage;
    }
}