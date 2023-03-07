package com.zouzhao.opt.manage.api;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

/**
 * <p>偏移量处理接口</p>
 * @author 姚超
 * @DATE: 2023-3-6
 */
public interface IOffsetApi {
    /**
     * <p>重置偏移量</p>
     *
     * @param consumer  消费者实例
     * @param message   消息
     * @param exception 异常
     */
    void resetOffset(Consumer<?, ?> consumer, Message<?> message, ListenerExecutionFailedException exception);

    /**
     * <p>重置偏移量（适用于批处理）</p>
     *
     * @param consumer  消费者实例
     * @param message   消息
     * @param exception 异常
     */
    void batchResetOffset(Consumer<?, ?> consumer, Message<?> message, ListenerExecutionFailedException exception);
}
