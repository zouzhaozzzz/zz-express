package com.zouzhao.opt.manage.core.controller;

import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.controller.PageController;
import com.zouzhao.opt.manage.api.IOptExportApi;
import com.zouzhao.opt.manage.dto.OptExportConditionVO;
import com.zouzhao.opt.manage.dto.OptExportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
@RestController
@RequestMapping("/data/opt-manage/optExport")
@Api(
       tags = "导入导出服务"
)
@PreAuthorize("hasAnyRole('OPT_MANAGE_EXPOTR_DEFAULT','OPT_MANAGE_EXPOTR_ADMIN')")
public class OptExportController extends BaseController<IOptExportApi, OptExportVO> implements PageController<IOptExportApi, OptExportVO> {

    @Autowired
    private IOptExportApi optExportApi;

    @PostMapping("/exportSends")
    @ApiOperation("导出快递")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPOTR_EXPORT','OPT_MANAGE_EXPOTR_ADMIN')")
    public void exportSends(@RequestBody OptExportConditionVO vo){
        optExportApi.exportSends();
    }

    @PostMapping("/importSends")
    @ApiOperation("导入快递")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPOTR_IMPORT','OPT_MANAGE_EXPOTR_ADMIN')")
    public void importSends(){
        optExportApi.importSends();
    }
}
