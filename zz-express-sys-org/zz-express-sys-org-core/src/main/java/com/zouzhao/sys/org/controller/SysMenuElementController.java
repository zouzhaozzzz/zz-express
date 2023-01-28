package com.zouzhao.sys.org.controller;

import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.CombineController;
import com.zouzhao.common.dto.Response;
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
    public Response<List<SysMenuElementVO>> listInRoles(@RequestBody SysMenuElementVO vo){
        return Response.ok(getApi().listInRoles(vo));
    }

    @PostMapping({"list"})
    @ApiOperation("列表查询接口")
    public Response<List<SysMenuElementVO>> list(@RequestBody SysMenuElementVO request) {
        Response<List<SysMenuElementVO>> list = CombineController.super.list(request);
        return  list;
    }
}
