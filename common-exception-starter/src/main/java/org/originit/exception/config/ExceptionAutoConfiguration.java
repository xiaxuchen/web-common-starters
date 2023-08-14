package org.originit.exception.config;

import org.originit.exception.handler.CommonExceptionHandler;
import org.originit.exception.log.DefaultExceptionLogger;
import org.originit.exception.log.ExceptionLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author xxc
 */
@Configuration
@Import({CommonExceptionHandler.class})
public class ExceptionAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(ExceptionLogger.class)
    public ExceptionLogger defaultExceptionLogger() {
        return new DefaultExceptionLogger();
    }

}
