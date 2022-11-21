package org.originit.redismq.annotation;

import java.lang.annotation.*;

/**
 * @author xxc
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisMQMessageListener {

    String DEFAULT_CONSUMER_GROUP = "DEFAULT_GROUP";

    /**
     * 消费者组
     */
    String consumerGroup() default DEFAULT_CONSUMER_GROUP;

    /**
     * Topic name.
     */
    String topic();
}
