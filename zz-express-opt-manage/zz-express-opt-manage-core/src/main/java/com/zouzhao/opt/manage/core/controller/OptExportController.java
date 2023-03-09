package com.zouzhao.opt.manage.core.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.controller.PageController;
import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.opt.manage.api.IOptExportApi;
import com.zouzhao.opt.manage.api.IOptFileApi;
import com.zouzhao.opt.manage.dto.OptExportConditionVO;
import com.zouzhao.opt.manage.dto.OptExportVO;
import com.zouzhao.opt.manage.dto.OptFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
@RestController
@RequestMapping("/data/opt-manage/optExport")
@Api(
       tags = "导入导出服务"
)
@PreAuthorize("hasAnyRole('OPT_MANAGE_EXPORT_DEFAULT','OPT_MANAGE_EXPORT_ADMIN')")
public class OptExportController extends BaseController<IOptExportApi, OptExportVO> implements PageController<IOptExportApi, OptExportVO> {

    @Autowired
    private IOptExportApi optExportApi;
    @Autowired
    private IOptFileApi optFileApi;


    @PostMapping("/exportSends")
    @ApiOperation("导出快递")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPORT_EXPORT','OPT_MANAGE_EXPORT_ADMIN')")
    public void exportSends(@RequestBody OptExportConditionVO vo){
        optExportApi.exportSends();
    }

    @PostMapping("/importSends")
    @ApiOperation("导入快递")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPORT_IMPORT','OPT_MANAGE_EXPORT_ADMIN')")
    public ResponseEntity<String> importSends(@RequestBody OptExportVO vo){
        String fileId = vo.getExportFileId();
        //检查文件
        if(StrUtil.isBlank(fileId))throw new MyException("没有传入附件id");
        OptFileVO fileVO = optFileApi.findVOById(IdDTO.of(fileId));
        if(fileVO == null || fileVO.getFilePath() == null) throw new MyException("文件丢失");
        String path = fileVO.getFilePath();
        //开始导入
        vo.setExportStartTime(new Date());
        //新增导入记录
        IdDTO dto = getApi().add(vo);
        optExportApi.importSends(path,dto.getId());
        return new ResponseEntity<>("导入已开始",HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPORT_LIST','OPT_MANAGE_EXPORT_ADMIN')")
    public Page<OptExportVO> page(Page<OptExportVO> page) {
        return PageController.super.page(page);
    }
}
