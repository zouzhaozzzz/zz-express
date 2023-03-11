package com.zouzhao.opt.file.core.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.aliyun.oss.OSSClient;
import com.zouzhao.common.core.service.PageServiceImpl;
import com.zouzhao.common.security.utils.RedisManager;
import com.zouzhao.opt.file.api.IOptExportApi;
import com.zouzhao.opt.file.core.entity.OptExport;
import com.zouzhao.opt.file.core.mapper.OptExportMapper;
import com.zouzhao.opt.file.dto.OptExportVO;
import com.zouzhao.opt.manage.client.OptExpressClient;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@Service
@Data
@RestController
@RequestMapping("/api/opt-file/optExport")
public class OptExportService extends PageServiceImpl<OptExportMapper, OptExport, OptExportVO> implements IOptExportApi {

    private static final Logger log = LoggerFactory.getLogger(OptExportService.class);

    @Value("${export.batchSize}")
    private int batchSize;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private OptExpressClient optExpressClient;
    @Autowired
    private OssService ossService;
    @Autowired
    private RedisManager redisManager;

    @Override
    public void exportSends() {
        //查询总数
        // List<OptExpressVO> data = optExpressClient.findAll(new OptExpressVO());

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
        // if (data.size() > 0) {
        //     String jsonStr = JSONUtil.toJsonStr(data);
        //     kafkaTemplate.send("sendExport", "test", jsonStr);
        //     data.clear();
        // }
        // kafkaTemplate.send("sendExport", "test", "endExport");
    }

    @Override
    @Transactional
    public void importSends(String filepath,String exportId) {
        //根据路径去阿里云拿到文件流和客户端连接
        List<Object> oss = ossService.downloadStream(filepath);
        OSSClient ossClient = (OSSClient) oss.get(1);
        try (
                InputStream inputStream = (InputStream) oss.get(0);
        ) {
            List<OptExpressVO> data = new ArrayList<>();
            //读excel
            EasyExcel.read(inputStream, OptExpressVO.class, new PageReadListener<OptExpressVO>(dataList -> {
                for (OptExpressVO express : dataList) {
                    data.add(express);
                    if (data.size() == batchSize) {
                        String jsonStr = JSONUtil.toJsonStr(data);
                        kafkaTemplate.send("sendImport", exportId, jsonStr);
                        log.debug("发送成功");
                        data.clear();
                    }
                }
            })).sheet().doRead();
            //最后一波数据可能小于batch_size
            if (data.size() > 0) {
                String jsonStr = JSONUtil.toJsonStr(data);
                kafkaTemplate.send("sendImport", exportId,jsonStr);
                log.debug("发送成功");
                data.clear();
            }
            kafkaTemplate.send("sendImport", exportId,"end");
        } catch (Exception e) {
            redisManager.appendStrValue("import-err:" + exportId,e.getMessage());
            updateJustFinish(exportId);
            e.printStackTrace();
        }finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    @Transactional
    public void updateJustFinish(String exportId) {
        //更新导入记录 完成时间 根据redis记录增加导入信息
        String err = redisManager.getValue("import-err:" + exportId);
        if(err != null){
            getMapper().updateJustFinish(exportId,new Date(),err);
        }else {
            StringBuilder builder = new StringBuilder("msg:");
            String redisKey="import-all:" + exportId;
            String all = redisManager.getValue(redisKey);
            if(all !=null) {
                builder.append("Excel中扫描到").append(all).append("条数据 ");
                redisManager.deleteKey(redisKey);
            }
            redisKey="import-success:" + exportId;
            String repeat = redisManager.getValue(redisKey);
            if(repeat !=null) {
                builder.append("成功导入").append(repeat).append("条数据 ");
                redisManager.deleteKey(redisKey);
            }
            getMapper().updateJustFinish(exportId,new Date(),builder.toString());
        }
    }


    @Override
    protected OptExport voToEntity(OptExportVO vo) {
        OptExport export = new OptExport();
        BeanUtils.copyProperties(vo, export);
        return export;
    }
}
