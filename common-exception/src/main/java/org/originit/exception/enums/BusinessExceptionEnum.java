package org.originit.exception.enums;

import org.originit.exception.*;
import org.springframework.http.HttpStatus;

/**
 * @author zhumaer
 * @desc 异常、HTTP状态码、默认自定义返回码 映射类
 * @since 9/21/2017 14:11 PM
 */
public enum BusinessExceptionEnum {

    /**
     * 无效参数
     */
    PARAMETER_INVALID(ParameterInvalidException.class, HttpStatus.BAD_REQUEST, ResultCode.PARAM_IS_INVALID),

    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(InternalServerException.class, HttpStatus.INTERNAL_SERVER_ERROR, ResultCode.SYSTEM_INNER_ERROR);

    private Class<? extends BusinessException> eClass;

    private HttpStatus httpStatus;

    private ResultCode resultCode;

    BusinessExceptionEnum(Class<? extends BusinessException> eClass, HttpStatus httpStatus, ResultCode resultCode) {
        this.eClass = eClass;
        this.httpStatus = httpStatus;
        this.resultCode = resultCode;
    }

    public Class<? extends BusinessException> getEClass() {
        return eClass;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public static boolean isSupportHttpStatus(int httpStatus) {
        for (BusinessExceptionEnum exceptionEnum : BusinessExceptionEnum.values()) {
            if (exceptionEnum.httpStatus.value() == httpStatus) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSupportException(Class<?> z) {
        for (BusinessExceptionEnum exceptionEnum : BusinessExceptionEnum.values()) {
            if (exceptionEnum.eClass.equals(z)) {
                return true;
            }
        }

        return false;
    }

    public static BusinessExceptionEnum getByHttpStatus(HttpStatus httpStatus) {
        if (httpStatus == null) {
            return null;
        }

        for (BusinessExceptionEnum exceptionEnum : BusinessExceptionEnum.values()) {
            if (httpStatus.equals(exceptionEnum.httpStatus)) {
                return exceptionEnum;
            }
        }

        return null;
    }

    public static BusinessExceptionEnum getByEClass(Class<? extends BusinessException> eClass) {
        if (eClass == null) {
            return null;
        }

        for (BusinessExceptionEnum exceptionEnum : BusinessExceptionEnum.values()) {
            if (eClass.equals(exceptionEnum.eClass)) {
                return exceptionEnum;
            }
        }

        return null;
    }
}
