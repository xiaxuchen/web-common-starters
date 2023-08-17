package org.originit.web.exception;


import com.originit.response.result.ResultGenerator;
import org.originit.exception.result.ExceptionResultGenerator;

import javax.annotation.Resource;

public class DefaultExceptionResultGenerator implements ExceptionResultGenerator {

    @Resource
    ResultGenerator resultGenerator;

    @Override
    public Object generate(String msg, Integer code) {
        return resultGenerator.fail(code, msg);
    }
}
