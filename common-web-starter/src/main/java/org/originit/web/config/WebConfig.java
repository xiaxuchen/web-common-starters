package org.originit.web.config;

import org.originit.exception.result.ExceptionResultGenerator;
import org.originit.web.exception.DefaultResultGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
public class WebConfig {

    /**
     * 默认的异常返回，使用PlatformResult
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ExceptionResultGenerator.class)
    public ExceptionResultGenerator exceptionResultGenerator() {
        return new DefaultResultGenerator();
    }
}
