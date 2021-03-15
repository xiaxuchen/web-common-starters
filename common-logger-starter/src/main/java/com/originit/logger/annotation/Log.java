package com.originit.logger.annotation;

/**
 * 添加该注解的类或方法会打印参数以及响应的日志
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标志所标注的类的所有方法打印日志，如果有不需要打印的可以使用{@link NoLog}注解标注
 * 标注所在的方法打印日志
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
}