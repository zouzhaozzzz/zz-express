package com.zouzhao.opt.manage.core.kafka;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.common.security.utils.RedisManager;
import com.zouzhao.opt.file.client.OptExportClient;
import com.zouzhao.opt.file.dto.OptExportConditionVO;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.core.service.OssService;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 姚超
 * @DATE: 2023-3-8
 */
@Service
@Data
public class KafkaService {

    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);
    private Map<String, ThreadLocal<ExcelWriter>> threadLocalMap = new HashMap<>();
    private ThreadLocal<OSS> threadLocal = new ThreadLocal<>();
    private ThreadLocal<InputStream> isThreadLocal = new ThreadLocal<>();

    @Value("${export.batchSize}")
    private int batchSize;
    @Value("${oss.bucketName}")
    private String bucketName;

    @Autowired
    private OptExportClient optExportClient;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private IOptExpressApi optExpressApi;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private OssService ossService;

    @KafkaListener(
            topics = {"sendExport"},
            groupId = "consumer-group-sendExport"
            // containerFactory = "batchKafkaListenerContainerFactory",
            // concurrency = "3"
            // errorHandler = "batchErrorBus" // 进行offset（偏移量的修复）
    )
    @Transactional
    public void handleExport(String data, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        try {
            List<OptExpressVO> expressVOList = new ArrayList<>();
            OptExportConditionVO conditionVO = JSONUtil.toBean(data, OptExportConditionVO.class);
            optExpressApi.pageQueryByCondition(conditionVO, resultContext -> {
                OptExpressVO optExpressVO = resultContext.getResultObject();
                expressVOList.add(optExpressVO);
                if (expressVOList.size() == batchSize) {
                    try {
                        //写excel
                        writeExcel(key, expressVOList, false);
                    } catch (Exception e) {
                        //移除write，保存失败原因
                        redisManager.appendStrValue("export-err:" + key, e.getMessage());
                        removeThreadLocalAndSave(key);
                        e.printStackTrace();
                    }
                }
            });
            if (expressVOList.size() > 0) {
                //写excel
                writeExcel(key, expressVOList, true);
            }
            //写完excel，关闭write流,上传文件
            //更新导出记录 完成时间 根据redis记录增加导入信息
            removeThreadLocalAndSave(key);
        } catch (Exception e) {
            //移除write，保存失败原因
            redisManager.appendStrValue("export-err:" + key, e.getMessage());
            removeThreadLocalAndSave(key);
            e.printStackTrace();
        }
    }

    //写excel
    private void writeExcel(String key, List<OptExpressVO> expressVOList, Boolean endFlag) throws IOException {
        //拿到输出文件流
        try (
                ByteArrayOutputStream os = new ByteArrayOutputStream()
        ) {
            ExcelWriter excelWriter = getExcelWriter(key, os);
            WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
            excelWriter.fill(expressVOList, writeSheet);
            //阿里云分片上传
            if (endFlag) {
                excelWriter.finish();
                uploadOss(key, os);
            }
            //在redis中放入成功数
            if (expressVOList.size() > 0) incrementNum("export-success:" + key, expressVOList.size());
            expressVOList.clear();
        }
    }

    //阿里云分片上传
    private void uploadOss(String key, ByteArrayOutputStream os) throws IOException {
        if (os.size() < 1) throw new MyException("输出流为空");
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        //获取连接，上传文件
        OSS ossClient = getOssClient();
        //拿到文件名
        String filename = (String) redisManager.getValue("export-filename:" + key);
        // 创建InitiateMultipartUploadRequest对象。
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, filename);
        // 初始化分片。
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识。您可以根据该uploadId发起相关的操作，例如取消分片上传、查询分片上传等。
        String uploadId = upresult.getUploadId();
        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags =  new ArrayList<PartETag>();
        // 每个分片的大小，用于计算文件有多少个分片。单位为字节。
        final long partSize = 1 * 1024 * 1024L;   //1 MB。
        long fileLength = is.available();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            // 跳过已经上传的分片。
            is.skip(startPos);
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(filename);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(is);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
            uploadPartRequest.setPartSize(curPartSize);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
            uploadPartRequest.setPartNumber( i + 1);
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
            partETags.add(uploadPartResult.getPartETag());
        }

        // 创建CompleteMultipartUploadRequest对象。
        // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, filename, uploadId, partETags);

        // 如果需要在完成分片上传的同时设置文件访问权限，请参考以下示例代码。
        // completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.Private);
        // 指定是否列举当前UploadId已上传的所有Part。如果通过服务端List分片数据来合并完整文件时，以上CompleteMultipartUploadRequest中的partETags可为null。
        // Map<String, String> headers = new HashMap<String, String>();
        // 如果指定了x-oss-complete-all:yes，则OSS会列举当前UploadId已上传的所有Part，然后按照PartNumber的序号排序并执行CompleteMultipartUpload操作。
        // 如果指定了x-oss-complete-all:yes，则不允许继续指定body，否则报错。
        // headers.put("x-oss-complete-all","yes");
        // completeMultipartUploadRequest.setHeaders(headers);

        // 完成分片上传。
        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        System.out.println(completeMultipartUploadResult.getETag());
    }



    private OSS getOssClient() {
        OSS ossClient = threadLocal.get();
        if (ossClient == null) {
            ossClient = ossService.getOssClient();
            threadLocal.set(ossClient);
            return ossClient;
        } else return ossClient;
    }


    @KafkaListener(
            topics = {"sendImport"},
            groupId = "consumer-group-sendImport"
            // containerFactory = "batchKafkaListenerContainerFactory",
            // concurrency = "3"
            // errorHandler = "batchErrorBus" // 进行offset（偏移量的修复）
    )
    @Transactional
    public void handleImport(String data, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
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

    //拿到写excel流
    private ExcelWriter getExcelWriter(String key, ByteArrayOutputStream os) throws IOException {
        ExcelWriter excelWriter;
        //判断是否有write
        if (threadLocalMap.get(key) == null || threadLocalMap.get(key).get() == null) {
            //模版
            //根据路径去阿里云拿到文件流和客户端连接
            InputStream inputStream = getExcelTemplate();
            excelWriter = EasyExcel.write(os, OptExpressVO.class).withTemplate(inputStream).build();
            ThreadLocal<ExcelWriter> excelWriterLocal = new ThreadLocal<>();
            excelWriterLocal.set(excelWriter);
            threadLocalMap.put(key, excelWriterLocal);

        } else {
            excelWriter = threadLocalMap.get(key).get();
        }
        return excelWriter;
    }

    private InputStream getExcelTemplate() {
        InputStream inputStream = isThreadLocal.get();
        if (inputStream == null) {
            OSS ossClient = getOssClient();
            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
            OSSObject ossObject = ossClient.getObject(bucketName, "template/快递导出模版.xlsx");
            // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
            InputStream content = ossObject.getObjectContent();
            isThreadLocal.set(content);
            return content;
        }
        return inputStream;
    }


    private void removeThreadLocalAndSave(String key) {
        try {
            //关闭连接
            OSS ossClient = threadLocal.get();
            if (ossClient != null) {
                ossClient.shutdown();
                threadLocal.remove();
            }
            ExcelWriter excelWriter = threadLocalMap.get(key).get();
            if (excelWriter != null) {
                excelWriter.close();
                threadLocalMap.remove(key);
            }
            InputStream inputStream = isThreadLocal.get();
            if(inputStream != null){
                inputStream.close();
                isThreadLocal.remove();
            }
            optExportClient.updateExportJustFinish(key);
        } catch (Exception e) {
            //保存失败原因
            redisManager.appendStrValue("import-err:" + key, e.getMessage());
            optExportClient.updateExportJustFinish(key);
            e.printStackTrace();
        }
    }


    private void incrementNum(String redisKey, Integer size) {
        Object num = redisManager.getValue(redisKey);
        if (ObjectUtil.isEmpty(num)) {
            redisManager.setValue(redisKey, size);
        } else {
            int newNum = (int) num + size;
            redisManager.setValue(redisKey, newNum);
        }
    }
}
