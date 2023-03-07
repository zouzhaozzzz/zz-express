package com.zouzhao.opt.manage.core.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectListing;
import com.zouzhao.common.core.exception.MyException;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@Log4j2
@Data
@ConfigurationProperties(prefix = "oss")
public class OssService {


    // @Value("${file.upload.dir}")
    // private String realPath;

    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
    // 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
    // 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
    // endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
    // 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
    private String endpoint;
    // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
    // 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
    // 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
    private String accessKeyId;
    private String accessKeySecret;
    // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
    private String bucketName;
    // Object是OSS存储数据的基本单元，称为OSS的对象，也被称为OSS的文件。详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Object命名规范如下：使用UTF-8编码，长度必须在1-1023字节之间，不能以“/”或者“\”字符开头。
    // private static String firstKey = "my-first-key";
    private String dir;



    // //清除冗余附件
    // @DeleteMapping("/deletePic")
    // public String deletePic() {
    //     List<Movie> movieList = movieService.findAll();
    //     List<Attachment> attachmentList = attachmentService.findNotInMovie(movieList);
    //     log.debug("要删除的附件:{}", attachmentList);
    //     //删除文件io流
    //     for (Attachment attachment : attachmentList) {
    //         File file = new File(realPath, attachment.getPath());
    //         if (file.exists()) {
    //             file.delete();
    //             //删除数据库路径信息
    //             attachmentService.deleteAttachment(attachment.getPath());
    //         }
    //     }
    //     return "success";
    // }


    /**
     * 列举文件、并删除多余文件
     */
    public void deleteUnnecessary() {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ObjectListing objectListing = ossClient.listObjects(bucketName);
            List<String> keyList=new ArrayList<>();
            // Map<String,Movie> movieMap = movieService.findAllPathMap();
            // for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            //     String key = objectSummary.getKey();
            //     String keyPath="https://" + bucketName + "." + endpoint.split("http://")[1] + "/" +key;
            //     if(movieMap.get(keyPath) == null && !Objects.equals(key, "movie/")) keyList.add(key);
            // }
            // log.error("keyList:{}",keyList);
            // if (keyList.size() >0)ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keyList).withEncodingType("url"));
            // log.error("删除成功");

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 上传文件
     *
     * @return
     */
    public String upload(MultipartFile file) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            String filename = dir + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + new Random().nextInt(10) + file.getOriginalFilename();
            ossClient.putObject(bucketName, filename, file.getInputStream());
            return "https://" + bucketName + "." + endpoint.split("http://")[1] + "/" + filename;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param filename
     * @param response
     */
    public void download(String filename, HttpServletResponse response) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
            OSSObject ossObject = ossClient.getObject(bucketName, filename);
            // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
            InputStream content = ossObject.getObjectContent();
            if (content != null) {
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                ServletOutputStream out = response.getOutputStream();
                FileCopyUtils.copy(content, out);
                content.close();
            }else throw new MyException("文件不存在");
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


}
