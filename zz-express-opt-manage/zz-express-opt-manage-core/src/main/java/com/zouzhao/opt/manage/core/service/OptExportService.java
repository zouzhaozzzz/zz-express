package com.zouzhao.opt.manage.core.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.zouzhao.opt.manage.api.IOptExportApi;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@Service
@Data
@ConfigurationProperties(prefix = "export")
@RestController("/api/opt-manage/optExport")
public class OptExportService implements IOptExportApi {

    private static final Logger log = LoggerFactory.getLogger(OptExportService.class);
    private String filepath;
    private int batchSize;

    private ThreadLocal<ExcelWriter> threadLocal = new ThreadLocal<>();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private IOptExpressApi optExpressApi;

    @Override
    public void exportSends() {
        //查询总数
        List<OptExpressVO> data = optExpressApi.findAll(new OptExpressVO());

        // List<SendDto> data = new ArrayList<>();
        //
        //
        //
        // sendMapper.pageQueryByScanTime(start, end, resultContext -> {
        //             SendDto SendDto = resultContext.getResultObject();
        //             data.add(SendDto);
        //             // log.debug("sendDto:{}",SendDto);
        //             if (data.size() == BATCH_SIZE) {
        //                 String jsonStr = JSONUtil.toJsonStr(data);
        //                 watch.start("kafka发送消息");
        //                 kafkaTemplate.send("sendExportX", "test", jsonStr);
        //                 watch.stop();
        //                 log.debug("发送成功");
        //                 data.clear();
        //             }
        //         }
        // );
        if (data.size() > 0) {
            String jsonStr = JSONUtil.toJsonStr(data);
            kafkaTemplate.send("sendExport", "test", jsonStr);
            data.clear();
        }
        kafkaTemplate.send("sendExport","test","endExport");
    }

    @Override
    @Transactional
    public void importSends() {
        String fileName =filepath +  "demo.xlsx";
        List<OptExpressVO> data=new ArrayList<>();
        //读excel
        EasyExcel.read(fileName, OptExpressVO.class, new PageReadListener<OptExpressVO>(dataList -> {
            for (OptExpressVO express : dataList) {
                log.info("读取到一条数据{}", express);
                data.add(express);
                if (data.size() == batchSize) {
                    String jsonStr = JSONUtil.toJsonStr(data);
                    kafkaTemplate.send("sendImport", "test", jsonStr);
                    log.debug("发送成功");
                    data.clear();
                }
            }
        })).sheet().doRead();
        //最后一波数据可能小于batch_size
        if (data.size() > 0) {
            String jsonStr = JSONUtil.toJsonStr(data);
            kafkaTemplate.send("sendImport", "test", jsonStr);
            log.debug("发送成功");
            data.clear();
        }
    }


    @KafkaListener(
            topics = {"sendExport"},
            groupId = "consumer-group-sendExport",
            containerFactory = "batchKafkaListenerContainerFactory",
            concurrency = "3",
            errorHandler = "batchErrorBus" // 进行offset（偏移量的修复）
    )
    public void batchHandleExport(List<String> data, Acknowledgment ack) {
        try {
            System.out.println(Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + ">>>" + data.size());
            for (int i = 0; i < data.size(); i++) {
                String dataEntry = data.get(i);
                //判断文件是否写完

                ExcelWriter excelWriter = threadLocal.get();
                if (ObjectUtils.isEmpty(excelWriter)) {
                    //模版
                    String templateFilename = filepath + "快递导出模版.xlsx";
                    String filename = filepath + "test.xlsx";
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
            e.printStackTrace();
        }
    }


    @KafkaListener(
            topics = {"sendImport"},
            groupId = "consumer-group-sendImport",
            containerFactory = "batchKafkaListenerContainerFactory",
            concurrency = "3",
            errorHandler = "batchErrorBus" // 进行offset（偏移量的修复）
    )
    public void batchHandleImport(List<String> data, Acknowledgment ack) {
        try {
            System.out.println(Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + ">>>" + data.size());
            for (int i = 0; i < data.size(); i++) {
                String dataEntry = data.get(i);
                List<OptExpressVO> list = JSONUtil.toList(dataEntry, OptExpressVO.class);
                //存储数据
                log.debug("kafka接收到导入数据的长度:{}",list.size());
                optExpressApi.batchSave(list);
            }
            ack.acknowledge(); // 已经消费完毕
        } catch (Exception e) {
            // 如果进入异常处理，那么kafka就不会收到客户端（当前消费消息的接口）发送的应答消息
            // 因此kafka会一直把这个消息保留(hold)在队列中
            // 客户端的下一次拉取仍然是拉取的上一次没有应答的消息
            // 避免了由于业务异常造成的消息“丢失“
            e.printStackTrace();
        }
    }
}
