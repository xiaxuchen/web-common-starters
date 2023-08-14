package org.originit.exception.result;

/**
 * 当出现异常时如何返回
 */
public interface ExceptionResultGenerator {

    Object generate(String msg,Integer code);

}
