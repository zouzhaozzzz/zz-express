package com.zouzhao.opt.manage.core.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.aliyun.oss.model.OSSObject;
import com.zouzhao.common.core.service.PageServiceImpl;
import com.zouzhao.opt.manage.api.IOptExportApi;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.core.entity.OptExport;
import com.zouzhao.opt.manage.core.mapper.OptExportMapper;
import com.zouzhao.opt.manage.dto.OptExportVO;
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
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
@RestController("/api/opt-manage/optExport")
public class OptExportService extends PageServiceImpl<OptExportMapper, OptExport, OptExportVO> implements IOptExportApi {

    private static final Logger log = LoggerFactory.getLogger(OptExportService.class);

    @Value("${export.batchSize}")
    private int batchSize;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private IOptExpressApi optExpressApi;
    @Autowired
    private OssService ossService;

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
        kafkaTemplate.send("sendExport", "test", "endExport");
    }

    @Override
    @Transactional
    public void importSends(String filepath,String exportId) {
        //根据路径去阿里云拿到文件流和客户端连接
        List<Object> oss = ossService.downloadStream(filepath);
        try (
                InputStream inputStream = (InputStream) oss.get(0);
                OSSObject ossObject = (OSSObject) oss.get(1);
        ) {
            List<OptExpressVO> data = new ArrayList<>();
            //读excel
            EasyExcel.read(inputStream, OptExpressVO.class, new PageReadListener<OptExpressVO>(dataList -> {
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
            kafkaTemplate.send("sendImport", "test", "end"+exportId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void updateFinishTimeById(String exportId) {
        getMapper().updateFinishTimeById(exportId,new Date());
    }


    @Override
    protected OptExport voToEntity(OptExportVO vo) {
        OptExport export = new OptExport();
        BeanUtils.copyProperties(vo, export);
        return export;
    }
}
