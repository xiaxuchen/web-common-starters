package org.originit.redismq.listener;

/**
 * Redis队列监听器
 * @param <T>
 */
public interface RedisMQListener<T> {

    void onMessage(String recordId,T message) throws Exception;

    Class<T> getType();
}
