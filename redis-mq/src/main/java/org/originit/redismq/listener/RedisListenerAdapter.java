package org.originit.redismq.listener;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.StandardCharsets;

public class RedisListenerAdapter implements MessageListener {

    private RedisListenerAdapter (){}

    private RedisMQListener redisMQListener;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private RedisListenerAdapter(RedisMQListener redisMQListener) {
        this.redisMQListener = redisMQListener;
    }

    public static MessageListener adapt(RedisMQListener<?> redisMQListener) {
        return new RedisListenerAdapter(redisMQListener);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        final String body = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            if (redisMQListener.getType().isAssignableFrom(String.class)) {
//                redisMQListener.onMessage(body);
            } else {
//                redisMQListener.onMessage(JSONUtil.toBean(body,redisMQListener.getType()));
            }
        }catch (Exception e) {
            return;
        }
    }
}
