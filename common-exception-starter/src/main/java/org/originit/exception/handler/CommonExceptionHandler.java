package org.originit.exception.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.originit.common.exception.NoTokenFoundException;
import org.originit.exception.BusinessException;
import org.originit.exception.enums.InnerResultCode;
import org.originit.exception.log.ExceptionLogger;
import org.originit.exception.result.ExceptionResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonExceptionHandler {

    @Autowired
    ExceptionResultGenerator resultGenerator;

    @Autowired
    ExceptionLogger exceptionLogger;


    /**
     * 通过resultCode生成响应
     */
    private Object generate(InnerResultCode resultCode) {
        return resultGenerator.generate(resultCode.message(), resultCode.code());
    }
    private Object generate(Integer code,String msg) {
        return resultGenerator.generate(msg,code);
    }

    private Object generate(InnerResultCode code, String msg) {
        return resultGenerator.generate(msg,code.code());
    }

    /**
     * 处理所有的异常
     * @return 友好的前端返回结果
     */
    @ExceptionHandler({Exception.class})
    public Object handleDefault(Exception e) {
        exceptionLogger.logException(e);
        return generate(InnerResultCode.REQUEST_ERROR,e.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        exceptionLogger.logException(e);
        return generate(InnerResultCode.REQUEST_METHOD_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        exceptionLogger.logException(illegalArgumentException);
        return generate(InnerResultCode.PARAM_IS_INVALID,illegalArgumentException.getMessage());
    }

    @ExceptionHandler(IllegalAccessException.class)
    public Object handleIllegalAccessException(IllegalAccessException e) {
        exceptionLogger.logException(e);
        return generate(InnerResultCode.PERMISSION_INVAILD,e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public Object handleIllegalStateException(IllegalStateException illegalStateException) {
        exceptionLogger.logException(illegalStateException);
        return generate(InnerResultCode.ILLEGAL_STATE,illegalStateException.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Object handleBussinessException(BusinessException e) {
        exceptionLogger.logBusinessException(e);
        Integer code = e.getCode();
        if (code == null) {
            code = InnerResultCode.SYSTEM_INNER_ERROR.code();
        }
        return generate(code,e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public Object handleMethodArgumentNotValidException(BindException e) {
        exceptionLogger.logException(e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return generate(InnerResultCode.PARAM_IS_INVALID,message);
    }


    @ExceptionHandler(NoTokenFoundException.class)
    public Object handleNoTokenFoundException(NoTokenFoundException e) {
        exceptionLogger.logException(e);
        return generate(InnerResultCode.TOKEN_NOT_FOUND,"用户未认证");
    }
    @ExceptionHandler({JWTVerificationException.class})
    public Object handleJWTErrorException(JWTVerificationException e) {
        exceptionLogger.logException(e);
        return generate(InnerResultCode.INVALID_TOKEN_ERROR,"用户认证信息错误");
    }

    @ExceptionHandler(TokenExpiredException.class)
    public Object handleTokenExpiredException(TokenExpiredException e) {
        exceptionLogger.logException(e);
        return generate(InnerResultCode.TOKEN_EXPIRED);
    }
}
