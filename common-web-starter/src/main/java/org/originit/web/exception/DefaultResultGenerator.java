package org.originit.web.exception;


import com.originit.response.result.PlatformResult;
import org.originit.exception.result.ExceptionResultGenerator;
public class DefaultResultGenerator implements ExceptionResultGenerator {

    @Override
    public Object generate(String msg, Integer code) {
        return PlatformResult.fail(code, msg);
    }
}
