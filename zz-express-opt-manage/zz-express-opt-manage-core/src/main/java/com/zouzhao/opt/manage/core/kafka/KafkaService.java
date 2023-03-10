package com.zouzhao.opt.manage.core.kafka;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.zouzhao.common.security.utils.RedisManager;
import com.zouzhao.opt.file.client.OptExportClient;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-8
 */
@Service
public class KafkaService {

    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);
    private ThreadLocal<ExcelWriter> threadLocal = new ThreadLocal<>();

    @Autowired
    private OptExportClient optExportClient;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private IOptExpressApi optExpressApi;

    @KafkaListener(
            topics = {"sendExport"},
            groupId = "consumer-group-sendExport"
            // containerFactory = "batchKafkaListenerContainerFactory",
            // concurrency = "3"
            // errorHandler = "batchErrorBus" // 进行offset（偏移量的修复）
    )
    @Transactional
    public void batchHandleExport(List<String> data, Acknowledgment ack) {
        try {
            System.out.println(Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + ">>>" + data.size());
            for (int i = 0; i < data.size(); i++) {
                String dataEntry = data.get(i);
                //判断文件是否写完

                ExcelWriter excelWriter = threadLocal.get();
                if (ObjectUtils.isEmpty(excelWriter)) {
                    //模版
                    String templateFilename = "快递导出模版.xlsx";
                    String filename = "test.xlsx";
                    excelWriter = EasyExcel.write(filename, OptExpressVO.class).withTemplate(templateFilename).build();
                    threadLocal.set(excelWriter);
                }
                if (dataEntry.equals("endExport")) {
                    threadLocal.remove();
                    excelWriter.close();
                    return;
                }
                List<OptExpressVO> list = JSONUtil.toList(dataEntry, OptExpressVO.class);

                WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
                excelWriter.fill(list, writeSheet);
            }
            ack.acknowledge(); // 已经消费完毕
        } catch (Exception e) {
            // 如果进入异常处理，那么kafka就不会收到客户端（当前消费消息的接口）发送的应答消息
            // 因此kafka会一直把这个消息保留(hold)在队列中
            // 客户端的下一次拉取仍然是拉取的上一次没有应答的消息
            // 避免了由于业务异常造成的消息“丢失“
            ack.acknowledge();
            e.printStackTrace();
        }
    }


    @KafkaListener(
            topics = {"sendImport"},
            groupId = "consumer-group-sendImport"
            // containerFactory = "batchKafkaListenerContainerFactory",
            // concurrency = "3"
            // errorHandler = "batchErrorBus" // 进行offset（偏移量的修复）
    )
    @Transactional
    public void batchHandleImport(String data, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        try {
            //判断文件是否全部导入完
            if (data.equals("end")) {
                //更新导入记录 完成时间 根据redis记录增加导入信息
                optExportClient.updateJustFinish(key);
            } else {
                List<OptExpressVO> list = JSONUtil.toList(data, OptExpressVO.class);
                //存储数据
                log.debug("kafka接收到导入数据的长度:{}", list.size());
                optExpressApi.batchSave(key, list);
            }
        } catch (Exception e) {
            // 如果进入异常处理，那么kafka就不会收到客户端（当前消费消息的接口）发送的应答消息
            // 因此kafka会一直把这个消息保留(hold)在队列中
            // 客户端的下一次拉取仍然是拉取的上一次没有应答的消息
            // 避免了由于业务异常造成的消息“丢失“
            redisManager.appendStrValue("import-err:" + key, e.getMessage());
            optExportClient.updateJustFinish(key);
            e.printStackTrace();
        }
    }
}
