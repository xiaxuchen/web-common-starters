package com.originit.logger.config;


import com.originit.logger.handler.LoggerAspect;
import com.originit.logger.property.LogProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "common.logger",name = "enable",havingValue = "true",matchIfMissing = true)
@EnableConfigurationProperties(LogProperty.class)
public class LoggerAutoConfiguration {

    @Bean
    public LoggerAspect loggerAspect (LogProperty property) {
        return new LoggerAspect(property);
    }
}