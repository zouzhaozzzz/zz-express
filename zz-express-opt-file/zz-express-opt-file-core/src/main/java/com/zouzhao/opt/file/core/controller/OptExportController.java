package com.zouzhao.opt.file.core.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.controller.PageController;
import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.security.utils.RedisManager;
import com.zouzhao.opt.file.api.IOptExportApi;
import com.zouzhao.opt.file.api.IOptFileApi;
import com.zouzhao.opt.file.dto.OptExportVO;
import com.zouzhao.opt.file.dto.OptFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
@RestController
@RequestMapping("/data/opt-file/optExport")
@Api(
        tags = "导入导出服务"
)
@PreAuthorize("hasAnyRole('OPT_MANAGE_EXPORT_DEFAULT','OPT_MANAGE_EXPORT_ADMIN')")
public class OptExportController extends BaseController<IOptExportApi, OptExportVO> implements PageController<IOptExportApi, OptExportVO> {

    @Autowired
    private IOptExportApi optExportApi;
    @Autowired
    private IOptFileApi optFileApi;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${oss.dir}")
    private String dir;

    @PostMapping("/exportSends")
    @ApiOperation("导出物流")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPORT_EXPORT','OPT_MANAGE_EXPORT_ADMIN')")
    public ResponseEntity<String> exportSends(@RequestBody OptExportVO vo) {
        //检查条件
        int count = vo.getExportCount();
        if (count == 0) throw new MyException("导出条件不正确");
        //开始导出
        vo.setExportStartTime(new Date());
        //新增导出记录，redis记录导出总数
        IdDTO dto = getApi().add(vo);
        redisManager.setValue("export-all:" + dto.getId(), count);
        //开始导出,告诉寄件服务要生成文件
        kafkaTemplate.send("sendExport", dto.getId(), JSONUtil.toJsonStr(vo.getCondition()));
        //在redis中放入导出的文件名
        String filename = dir + "物流导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + new Random().nextInt(10)+".xlsx";
        redisManager.setValue("export-filename:" + dto.getId(),filename );
        return new ResponseEntity<>("导出已开始", HttpStatus.OK);
    }


    @PostMapping("/importSends")
    @ApiOperation("导入物流")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPORT_IMPORT','OPT_MANAGE_EXPORT_ADMIN')")
    public ResponseEntity<String> importSends(@RequestBody OptExportVO vo) {
        String fileId = vo.getExportFileId();
        //检查文件
        if (StrUtil.isBlank(fileId)) throw new MyException("没有传入附件id");
        OptFileVO fileVO = optFileApi.findVOById(IdDTO.of(fileId));
        if (fileVO == null || fileVO.getFilePath() == null) throw new MyException("文件丢失");
        String path = fileVO.getFilePath();
        //开始导入
        vo.setExportStartTime(new Date());
        //新增导入记录
        IdDTO dto = getApi().add(vo);
        optExportApi.importSends(path, dto.getId());
        return new ResponseEntity<>("导入已开始", HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPORT_LIST','OPT_MANAGE_EXPORT_ADMIN')")
    public Page<OptExportVO> page(Page<OptExportVO> page) {
        PageController.super.page(page);
        page.getRecords().forEach(e -> {
            if (e.getExportFinishTime() != null) e.setExportSchedule(100.0);
            else {
                e.setExportSchedule(getSchedule(e.getExportId(), e.getExportType()));
            }
        });
        return page;
    }

    private double getSchedule(String id, Integer type) {
        if (ObjectUtil.isEmpty(id) || type == null) throw new MyException("获取进度没有传入信息");
        Object allStr;
        Object successStr;
        if (type == 0) {
            allStr = redisManager.getValue("import-all:" + id);
            successStr = redisManager.getValue("import-success:" + id);
        } else {
            allStr = redisManager.getValue("export-all:" + id);
            successStr = redisManager.getValue("export-success:" + id);
        }

        if (ObjectUtil.isEmpty(allStr) || ObjectUtil.isEmpty(successStr)) {
            return 0.0;
        } else {
            //保留一位小数
            BigDecimal var5 = BigDecimal.valueOf(Double.valueOf((Integer) successStr) / (Double.valueOf((Integer) allStr)) * 100);
            return var5.setScale(1, RoundingMode.DOWN).doubleValue();
        }
    }


}
