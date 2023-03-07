package com.zouzhao.opt.manage.core.service;

import com.zouzhao.opt.manage.api.IOffsetApi;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>偏移量处理服务类</p>
 * <ul>
 *     <li>重置偏移量</li>
 *     <li>错误消息入库</li>
 * </ul>
 *
 * @author 姚超
 * @DATE: 2023-3-2
 */
@Service
public class OffsetService implements IOffsetApi {

    //@Resource
    //private JdbcTemplate jdbcTemplate;

    //private static final String sql = "insert into t_kafka_error_msg(id, topic, content, error_msg, created_time) values(null,?,?,?,now())";

    @Override
    public void resetOffset(Consumer<?, ?> consumer, Message<?> message, ListenerExecutionFailedException exception) {
        MessageHeaders headers = message.getHeaders();
        // 获取基本字段
        String topic = headers.get(KafkaHeaders.RECEIVED_TOPIC, String.class);
        Integer partition = headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class);
        // 偏移量复位
        Long offset = headers.get(KafkaHeaders.OFFSET, Long.class);
        if (partition != null && offset != null)
            consumer.seek(new TopicPartition(topic, partition), offset);
        // 入error表
//        String content = message.getPayload().toString();
//        String errorMsg = exception.getMessage();
//        jdbcTemplate.update(sql, topic, content, errorMsg);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void batchResetOffset(Consumer<?, ?> consumer, Message<?> message, ListenerExecutionFailedException exception) {
        MessageHeaders headers = message.getHeaders();
        // 获取基本字段
        List<String> topics = headers.get(KafkaHeaders.RECEIVED_TOPIC, List.class);
        // 偏移量复位
        List<Integer> partitions = headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, List.class);
        List<Long> offsets = headers.get(KafkaHeaders.OFFSET, List.class);
        Map<TopicPartition, Long> offsetsToReset = new HashMap<>(); // 需重置的主题分区映射
        if (topics != null && partitions != null && offsets != null)
            for (int i = 0; i < topics.size(); i++) {
                int index = i;
                offsetsToReset.compute(new TopicPartition(topics.get(i), partitions.get(i)),
                        (k, v) -> v == null ? offsets.get(index) : Math.min(v, offsets.get(index)));
            }
        offsetsToReset.forEach(consumer::seek);
        // 入error表
//        String payload = message.getPayload().toString();
//        List<String> contents = JSON.parseArray(payload, String.class);
//        String errorMsg = exception.getMessage();
//        List<Object[]> list = new ArrayList<>();
//        for (int i = 0; i < contents.size(); i++) {
//            assert topics != null;
//            Object[] args = new Object[]{topics.get(i), contents.get(i), errorMsg};
//            list.add(args);
//        }
//        jdbcTemplate.batchUpdate(sql, list);
    }

//    @Override
//    @Transactional
//    public Long offset(String topic, Integer partiton) {
//        String sql = "select sub_topic_partition_offset from t_kafka_offset where sub_topic = ? and sub_topic_partition_id = ?";
//        Long offset = 0L;
//        try {
//            offset = jdbcTemplate.queryForObject(sql, Long.class, topic, partiton);
//        } catch (EmptyResultDataAccessException e) {
//
//        }
//
//        return offset;
//    }
//
//    @Override
//    @Transactional
//    public void process(ConsumerRecord<String, String> record) {
//        // 保存 offset
//        long maxOffset = record.offset() + 1;
//        String updateOffsetSql = "select count(1) from t_kafka_offset where   sub_topic = ? and sub_topic_partition_id = ?";
//
//        Integer count = jdbcTemplate.queryForObject(updateOffsetSql, Integer.class, record.topic(), record.partition());
//        if (count > 0) {
//            updateOffsetSql = "update t_kafka_offset set sub_topic_partition_offset = ?,sub_timestamp=now()" +
//                    " where   sub_topic = ? and sub_topic_partition_id = ?";
//
//        } else {
//            updateOffsetSql = "insert into t_kafka_offset(sub_topic_partition_offset,sub_topic,sub_topic_partition_id,sub_timestamp) values(?,?,?,now())";
//        }
//
//        jdbcTemplate.update(updateOffsetSql, maxOffset, record.topic(), record.partition());
//    }
//
//    public void setDataSource(DataSource dataSource) {
//        jdbcTemplate = new JdbcTemplate(dataSource);
//    }
}