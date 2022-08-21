package org.originit.exception.handler;

import org.originit.exception.BusinessException;
import org.originit.exception.enums.ResultCode;
import org.originit.exception.log.ExceptionLogger;
import org.originit.exception.result.ExceptionResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    private Object generate(ResultCode resultCode) {
        return resultGenerator.generate(resultCode.message(), resultCode.code());
    }
    private Object generate(Integer code,String msg) {
        return resultGenerator.generate(msg,code);
    }

    private Object generate(ResultCode code,String msg) {
        return resultGenerator.generate(msg,code.code());
    }

    /**
     * 处理所有的异常
     * @return 友好的前端返回结果
     */
    @ExceptionHandler({Exception.class})
    public Object handleDefault(Exception e) {
        exceptionLogger.logException(e);
        return generate(ResultCode.SYSTEM_INNER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        exceptionLogger.logException(illegalArgumentException);
        return generate(ResultCode.PARAM_IS_INVALID,illegalArgumentException.getMessage());
    }

    @ExceptionHandler(IllegalAccessException.class)
    public Object handleIllegalAccessException(IllegalAccessException e) {
        exceptionLogger.logException(e);
        return generate(ResultCode.PERMISSION_NO_ACCESS,e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public Object handleIllegalStateException(IllegalStateException illegalStateException) {
        exceptionLogger.logException(illegalStateException);
        return generate(ResultCode.ILLEGAL_STATE,illegalStateException.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Object handleBussinessException(BusinessException e) {
        exceptionLogger.logBussinessException(e);
        Integer code = e.getCode();
        if (code == null) {
            code = ResultCode.SYSTEM_INNER_ERROR.code();
        }
        return generate(code,e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        exceptionLogger.logException(e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return generate(ResultCode.PARAM_IS_INVALID,message);
    }
}
