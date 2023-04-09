package com.originit.common.param.anno;

import java.lang.annotation.*;

/**
 * @author xxc
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BodyField {

    public String value() default "";
}
