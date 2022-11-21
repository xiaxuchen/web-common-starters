package org.originit.redismq.config;

import lombok.extern.slf4j.Slf4j;
import org.originit.redismq.listener.RedisMQListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class RedisMQConfig {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Bean
    ExecutorService redisBlocking(ApplicationContext applicationContext) {
        log.info("【executor】当前有{}个listener",applicationContext.getBeanNamesForType(RedisMQListener.class).length);
        return Executors.newFixedThreadPool(applicationContext.getBeanNamesForType(RedisMQListener.class).length);
    }
}
