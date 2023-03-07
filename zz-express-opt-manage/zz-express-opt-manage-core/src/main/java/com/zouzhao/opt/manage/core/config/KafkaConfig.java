package com.zouzhao.opt.manage.core.config;

import com.zouzhao.opt.manage.api.IOffsetApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.Collection;

@Configuration
@EnableKafka
@EnableTransactionManagement // 开启事务管理
@Slf4j
public class KafkaConfig {

    @Resource
    private KafkaProperties properties;

    @Resource
    private IOffsetApi offsetService;

    private final ConsumerRebalanceListener consumerRebalanceListener = new ConsumerAwareRebalanceListener() {
        @Override
        public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
            System.out.println("...onPartitionsRevokedBeforeCommit");
            consumer.commitSync();
        }

        @Override
        public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
            System.out.println("...onPartitionsAssigned");
            for (TopicPartition topicPartition : partitions) {
                // Get the last committed offset for the given partition
                OffsetAndMetadata offsetAndMetadata = consumer.committed(topicPartition);
                if (offsetAndMetadata != null)
                    consumer.seek(topicPartition, offsetAndMetadata.offset());
                else
                    consumer.seek(topicPartition, 0);
            }
        }
    };

    /**
     * <p>消息发送异常处理总线</p>
     *
     * @return 错误处理机制
     */
    @Bean
    public ConsumerAwareListenerErrorHandler errorBus() {
        return (message, exception, consumer) -> {
            offsetService.resetOffset(consumer, message, exception);
            return null;
        };
    }

    /**
     * <p>消息批处理发送 - 异常处理总线</p>
     *
     * @return 错误处理机制
     */
    @Bean
    public ConsumerAwareListenerErrorHandler batchErrorBus() {
        return (message, exception, consumer) -> {
            offsetService.batchResetOffset(consumer, message, exception);
            return null;
        };
    }

    /**
     * Kafka批处理监听容器工厂Bean
     *
     * @param configurer           监听容器工厂配置对象
     * @param kafkaConsumerFactory Kafka消费者工厂
     * @return 工厂实例
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> batchKafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setBatchListener(true); // 开启批处理
        configurer.configure(factory, kafkaConsumerFactory
                .getIfAvailable(() -> new DefaultKafkaConsumerFactory<>(this.properties.buildConsumerProperties())));
        factory.getContainerProperties().setConsumerRebalanceListener(consumerRebalanceListener);
        return factory;
    }

}