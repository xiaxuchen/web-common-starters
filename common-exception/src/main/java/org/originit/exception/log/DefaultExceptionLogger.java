package org.originit.exception.log;

import com.originit.common.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.originit.exception.BusinessException;

/**
 * 默认的异常堆栈打印
 * @author xxc
 */
@Slf4j
public class DefaultExceptionLogger implements ExceptionLogger{


    @Override
    public void logException(Exception e) {
        log.error(ExceptionUtil.buildErrorMessage(e));
    }

    @Override
    public void logBussinessException(BusinessException businessException) {
        log.info(ExceptionUtil.buildErrorMessage(businessException));
    }
}
