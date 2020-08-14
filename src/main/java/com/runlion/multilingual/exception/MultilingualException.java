package com.runlion.multilingual.exception;


import com.runlion.multilingual.enums.BizMultilingualStatusEnum;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

/**
 * 基础异常
 *
 * @author wangyiting
 * @version 1.0 created 2019/12/4
 */
public class MultilingualException extends RuntimeException {
    private int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    private Object[] params;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MultilingualException() {
    }

    public MultilingualException(String message, int status) {
        super(message);
        this.status = status;
    }

    private MultilingualException(String message, String status) {
        super(message);
        this.status = Integer.parseInt(status);
    }

    public MultilingualException(String message) {
        super(message);
    }

    public MultilingualException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultilingualException(Throwable cause) {
        super(cause);
    }

    public MultilingualException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    private MultilingualException(int code, String message, Throwable cause) {
        super(message, cause);
        this.status = code;
    }

    public MultilingualException(BizMultilingualStatusEnum result) {
        super(result.getMessage());
        status = result.getStatus();
    }

    public MultilingualException(BizMultilingualStatusEnum result, Throwable cause) {
        this(result.getStatus(), result.getMessage(), cause);
    }

    public MultilingualException(BizMultilingualStatusEnum result, Object... params) {
        this(MessageFormat.format(result.getMessage(), params), result.getStatus());
        this.params = params;
    }

    public MultilingualException(BizMultilingualStatusEnum result, Throwable cause, Object... params) {
        this(result.getStatus(), MessageFormat.format(result.getMessage(), params), cause);
        this.params = params;
    }
}
