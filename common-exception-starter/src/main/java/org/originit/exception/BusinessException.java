package org.originit.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhumaer
 * @desc 业务异常类
 * @since 9/18/2017 3:00 PM
 */
@Data
public class BusinessException extends RuntimeException {

    protected Integer code;

    protected Object data;


    public BusinessException(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Integer code, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public BusinessException(String message, Throwable cause, Integer code, Object data) {
        super(message, cause);
        this.code = code;
        this.data = data;
    }

    public BusinessException(Throwable cause, Integer code, Object data) {
        super(cause);
        this.code = code;
        this.data = data;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code, Object data) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.data = data;
    }
}
