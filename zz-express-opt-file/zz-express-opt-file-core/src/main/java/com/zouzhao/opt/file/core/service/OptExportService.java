package com.zouzhao.opt.file.core.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.aliyun.oss.OSSClient;
import com.zouzhao.common.core.service.PageServiceImpl;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.security.utils.RedisManager;
import com.zouzhao.opt.file.api.IOptExportApi;
import com.zouzhao.opt.file.api.IOptFileApi;
import com.zouzhao.opt.file.core.entity.OptExport;
import com.zouzhao.opt.file.core.mapper.OptExportMapper;
import com.zouzhao.opt.file.dto.OptExportVO;
import com.zouzhao.opt.file.dto.OptFileVO;
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
import java.util.concurrent.atomic.AtomicInteger;

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
    private OssService ossService;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private IOptFileApi optFileApi;


    @Override
    @Transactional
    public void importSends(String filepath, String exportId) {
        //根据路径去阿里云拿到文件流和客户端连接
        List<Object> oss = ossService.downloadStream(filepath);
        OSSClient ossClient = (OSSClient) oss.get(1);
        try (
                InputStream inputStream = (InputStream) oss.get(0);
        ) {
            List<OptExpressVO> data = new ArrayList<>();
            //统计总数放入redis中
            AtomicInteger count = new AtomicInteger();
            //读excel
            EasyExcel.read(inputStream, OptExpressVO.class, new PageReadListener<OptExpressVO>(dataList -> {
                for (OptExpressVO express : dataList) {
                    data.add(express);
                    if (data.size() == batchSize) {
                        count.addAndGet(batchSize);
                        String jsonStr = JSONUtil.toJsonStr(data);
                        kafkaTemplate.send("sendImport", exportId, jsonStr);
                        log.debug("发送成功");
                        data.clear();
                    }
                }
            })).sheet().doRead();
            //最后一波数据可能小于batch_size
            if (data.size() > 0) {
                count.addAndGet(data.size());
                String jsonStr = JSONUtil.toJsonStr(data);
                kafkaTemplate.send("sendImport", exportId, jsonStr);
                log.debug("发送成功");
                data.clear();
            }
            redisManager.setValue("import-all:" + exportId, count.get());
            kafkaTemplate.send("sendImport", exportId, "end");
        } catch (Exception e) {
            redisManager.appendStrValue("import-err:" + exportId, e.getMessage());
            updateJustFinish(exportId);
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    @Transactional
    public void updateJustFinish(String exportId) {
        //更新导入记录 完成时间 根据redis记录增加导入信息
        Object err = redisManager.getValue("import-err:" + exportId);
        if (err != null) {
            getMapper().updateJustFinish(exportId, new Date(), (String) err);
        } else {
            StringBuilder builder = new StringBuilder("msg:");
            String redisKey = "import-all:" + exportId;
            Object all = redisManager.getValue(redisKey);
            if (all != null) {
                builder.append("Excel中扫描到").append((int) all).append("条数据 ");
                redisManager.deleteKey(redisKey);
            }
            redisKey = "import-success:" + exportId;
            Object success = redisManager.getValue(redisKey);
            if (success != null) {
                builder.append("成功导入").append((int) success).append("条数据 ");
                redisManager.deleteKey(redisKey);
            }
            getMapper().updateJustFinish(exportId, new Date(), builder.toString());
        }
    }

    @Override
    @Transactional
    public void updateExportJustFinish(String exportId) {
        //更新导入记录 完成时间 文件路径 根据redis记录增加导入信息
        Object err = redisManager.getValue("export-err:" + exportId);
        if (err != null) {
            getMapper().updateExportJustFinish(exportId, new Date(), (String) err,null);
        } else {
            StringBuilder builder = new StringBuilder("msg:");
            String redisKey = "export-all:" + exportId;
            Object all = redisManager.getValue(redisKey);
            if (all != null) {
                builder.append("符合条件的数据总共有").append((int) all).append("条 ");
                redisManager.deleteKey(redisKey);
            }
            redisKey = "export-success:" + exportId;
            Object success = redisManager.getValue(redisKey);
            if (success != null) {
                builder.append("成功导出").append((int) success).append("条数据为Excel ");
                redisManager.deleteKey(redisKey);
            }
            //增加附件
            String filePath=redisManager.getValue("export-filename:")+exportId;
            OptFileVO optFileVO=new OptFileVO();
            optFileVO.setFilePath(filePath);
            IdDTO idDTO = optFileApi.add(optFileVO);
            getMapper().updateExportJustFinish(exportId, new Date(), builder.toString(),idDTO.getId());
        }
    }


    @Override
    protected OptExport voToEntity(OptExportVO vo) {
        OptExport export = new OptExport();
        BeanUtils.copyProperties(vo, export);
        return export;
    }
}
