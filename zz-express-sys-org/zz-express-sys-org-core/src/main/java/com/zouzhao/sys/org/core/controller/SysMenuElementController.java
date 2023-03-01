package com.zouzhao.sys.org.core.controller;

import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.CombineController;
import com.zouzhao.sys.org.api.ISysMenuElementApi;
import com.zouzhao.sys.org.dto.SysMenuElementVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-24
 */
@RestController
@RequestMapping("/data/sys-org/sysMenuElement")
@Api(
        tags = "菜单"
)
public class SysMenuElementController extends BaseController<ISysMenuElementApi, SysMenuElementVO> implements CombineController<ISysMenuElementApi, SysMenuElementVO> {

    @PostMapping("/listInRoles")
    // @Secured({"ROLE_DEFAULT"})
    public List<SysMenuElementVO> listInRoles(@RequestBody SysMenuElementVO vo){
        return getApi().listInRoles(vo);
    }

    @PostMapping({"/treeData"})
    @ApiOperation("列表查询接口")
    public List<SysMenuElementVO> list(@RequestBody SysMenuElementVO request) {
        return getApi().treeData(request);
    }
}
