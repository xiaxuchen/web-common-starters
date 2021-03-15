package com.originit.response.anotation;

import com.originit.response.result.PlatformResult;
import com.originit.response.result.Result;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于声明Controller使用哪个响应类，默认是PlatformResult
 * @author xxc、
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public@interface ResponseResult{
    Class<? extends Result> value() default PlatformResult.class;
}
