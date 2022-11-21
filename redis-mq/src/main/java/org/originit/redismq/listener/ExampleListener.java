package org.originit.redismq.listener;

import lombok.extern.slf4j.Slf4j;
import org.originit.redismq.annotation.RedisMQMessageListener;
import org.originit.redismq.domain.User;
import org.originit.redismq.template.RedisMQTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RedisMQMessageListener(topic = RedisMQTemplate.DEFAULT_LOG_DESTINATION)
public class ExampleListener implements RedisMQListener<User> {
    @Override
    public void onMessage(String recordId,User message) {
        log.info("【消费】打印日志:{}",message);
    }

    @Override
    public Class<User> getType() {
        return User.class;
    }
}
