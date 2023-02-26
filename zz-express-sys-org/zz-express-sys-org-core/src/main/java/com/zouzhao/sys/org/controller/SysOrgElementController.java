package com.zouzhao.sys.org.controller;

import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.PageController;
import com.zouzhao.sys.org.api.ISysOrgElementApi;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-2-17
 */
@RestController
@RequestMapping("/data/sys-org/sysOrgElement")
@Api(
        tags = "组织元素"
)
public class SysOrgElementController extends BaseController<ISysOrgElementApi, SysOrgElementVO> implements PageController<ISysOrgElementApi, SysOrgElementVO> {

    @PostMapping({"/treeData"})
    @ApiOperation("列表查询接口")
    public List<SysOrgElementVO> treeData(@RequestBody SysOrgElementVO request) {
        return getApi().treeData(request);
    }
}
