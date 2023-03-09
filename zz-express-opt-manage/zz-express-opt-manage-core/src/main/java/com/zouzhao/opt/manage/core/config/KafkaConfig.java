package com.zouzhao.opt.manage.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.RetryingBatchErrorHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.backoff.FixedBackOff;

import javax.annotation.Resource;

@Configuration
@EnableKafka
@EnableTransactionManagement // 开启事务管理
@Slf4j
public class KafkaConfig {

    @Resource
    private KafkaProperties properties;

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
        //最大重试15次
        RetryingBatchErrorHandler retryingBatchErrorHandler = new RetryingBatchErrorHandler(new FixedBackOff(500L, 10L),
                createConsumerRecordRecoverer());
        factory.setBatchErrorHandler(retryingBatchErrorHandler);

        return factory;
    }

    /**
     * 最终消费失败打印日志即可
     */
    private ConsumerRecordRecoverer createConsumerRecordRecoverer() {
        return (consumerRecord, exception) -> {
            log.error("consumer, exception:{}", exception.toString());
        };
    }

}