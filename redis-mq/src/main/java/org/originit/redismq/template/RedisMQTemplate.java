package org.originit.redismq.template;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.originit.redismq.annotation.RedisMQMessageListener;
import org.originit.redismq.listener.RedisMQListener;
import org.originit.redismq.util.RedisStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.Message;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xxc
 */
@Component
@Slf4j
public class RedisMQTemplate extends AbstractMessageSendingTemplate<String> implements InitializingBean {

    private static final String METHOD_NAME = "onMessage";
    private static final String DATA_FIELD = "DATA";
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    public static final String DEFAULT_LOG_DESTINATION = "log-queen";

    private List<RedisMQListener> redisMQListeners;

    private AtomicLong atomicLong = new AtomicLong(0);

    @Autowired
    RedisStream redisStream;

    @Autowired
    RedisMessageListenerContainer messageListenerContainer;

    @Autowired(required = false)
    public void setRedisMQListeners(List<RedisMQListener> redisMQListeners) {
        this.redisMQListeners = redisMQListeners;
    }

    @Override
    protected void doSend(String key, Message<?> message) {
        final HashMap<String, String> msg = new HashMap<>(2);
        if (message.getPayload() instanceof String) {
            msg.put(DATA_FIELD, ((String) message.getPayload()));
        } else {
            msg.put(DATA_FIELD,JSONUtil.toJsonStr(message.getPayload()));
        }
        redisTemplate.opsForStream().add(StreamRecords.newRecord()
                .in(key).withId(RecordId.of(System.currentTimeMillis(),atomicLong.incrementAndGet()))
                .ofMap(msg));
    }

    @Autowired
    @Qualifier("redisBlocking")
    ExecutorService executor;

    @Override
    public void afterPropertiesSet() throws Exception {
        setDefaultDestination(DEFAULT_LOG_DESTINATION);
        if (redisMQListeners == null) {
            redisMQListeners = Collections.EMPTY_LIST;
        }
        final HashMap<String, Set<String>> groupMap = new HashMap<>();
        for (RedisMQListener redisMQListener : this.redisMQListeners) {
            final RedisMQMessageListener annotation = redisMQListener.getClass().getAnnotation(RedisMQMessageListener.class);
            if (annotation == null) {
                log.warn("【redis mq】RedisMQListener {} should annotated RedisMQMessageListener",redisMQListener.getClass().getName());
                continue;
            }
            redisTemplate.execute(new SessionCallback<Void>() {
                @Override
                public Void execute(RedisOperations operations) throws DataAccessException {
                    operations.watch(annotation.topic());
                    operations.multi();
                    try {
                        final HashMap<String, String> map = new HashMap<>();
                        map.put("test","1");
                        operations.opsForStream().add(StreamRecords.newRecord().withId("0-0").in(annotation.topic()).ofMap(map));
                        operations.opsForStream().delete(annotation.topic(),"0-0");
                        operations.exec();
                    }catch (Exception e) {
                        e.printStackTrace();
                        operations.discard();
                    }finally {
                        operations.unwatch();
                    }
                    return null;
                }
            });

            if (!groupMap.containsKey(annotation.topic())) {
                groupMap.put(annotation.topic(),new HashSet<>());
            }
            final Set<String> groups = groupMap.get(annotation.topic());
            if (!groups.contains(annotation.consumerGroup())) {
                groups.add(annotation.consumerGroup());
                try {
                    redisTemplate.opsForStream().createGroup(annotation.topic(),annotation.consumerGroup());
                }catch (Exception e) {

                }
            }
            final StreamReadOptions options = StreamReadOptions.empty();
            options.block(Duration.of(3, ChronoUnit.MINUTES)).count(1);
            executor.submit(() -> {
                Consumer consumer = Consumer.from(annotation.consumerGroup(),redisMQListener.getClass().getName());
                while (true) {
                    final List<MapRecord<String, Object, Object>> message = redisTemplate.opsForStream().read(consumer,options, StreamOffset.create(annotation.topic(), ReadOffset.lastConsumed()));
                    if (message == null || message.isEmpty()) {
                        continue;
                    }
                    final MapRecord<String, Object, Object> msg = message.get(0);
                    if (redisMQListener.getType().isAssignableFrom(String.class)) {
                        redisMQListener.onMessage(msg.getId().getValue(),msg.getValue().get(RedisMQTemplate.DATA_FIELD).toString());
                    } else {
                        redisMQListener.onMessage(msg.getId().getValue(),JSONUtil.toBean(msg.getValue().get(RedisMQTemplate.DATA_FIELD).toString(),redisMQListener.getType()));
                    }
                }
            });
        }
    }
}
