package org.originit.et.anno;

import java.lang.annotation.*;

/**
 * 注释
 * @author xxc
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Comment {
    public String value();
}
