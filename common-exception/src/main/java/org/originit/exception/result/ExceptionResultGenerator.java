package org.originit.exception.result;

public interface ExceptionResultGenerator {

    Object generate(String msg,Integer code);

}
