package com.zouzhao.opt.manage.core.controller;

import com.zouzhao.opt.manage.api.IOptExportApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
public class OptExportController {

    @Autowired
    private IOptExportApi optExportApi;

    @PostMapping("/exportSends")
    @ApiOperation("导出快递")
    public void exportSends(){
        optExportApi.exportSends();
    }

    @PostMapping("/importSends")
    @ApiOperation("导入快递")
    public void importSends(){
        optExportApi.importSends();
    }
}
