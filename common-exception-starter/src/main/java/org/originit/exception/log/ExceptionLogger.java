package org.originit.exception.log;

import org.originit.exception.BusinessException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

@ConditionalOnClass
public interface ExceptionLogger {

    /**
     * 打印常规异常的信息
     * @param e 常规异常
     */
    void logException(Exception e);

    /**
     * 打印业务异常的信息
     * @param businessException 业务异常
     */
    void logBusinessException(BusinessException businessException);
}
