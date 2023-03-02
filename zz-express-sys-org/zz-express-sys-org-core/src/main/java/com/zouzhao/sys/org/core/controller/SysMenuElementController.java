package com.zouzhao.sys.org.core.controller;

import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.CombineController;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.sys.org.api.ISysMenuElementApi;
import com.zouzhao.sys.org.dto.SysMenuElementVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<SysMenuElementVO> listInRoles(@RequestBody SysMenuElementVO vo){
        return getApi().listInRoles(vo);
    }

    @PostMapping({"/treeData"})
    @ApiOperation("列表查询接口")
    @PreAuthorize("hasAnyRole('SYS_MENU_ELEMENT_ADMIN')")
    public List<SysMenuElementVO> list(@RequestBody SysMenuElementVO request) {
        return getApi().treeData(request);
    }

    @PostMapping({"/add"})
    @ApiOperation("新增接口")
    @PreAuthorize("hasAnyRole('SYS_MENU_ELEMENT_ADMIN')")
    public IdDTO add(@RequestBody SysMenuElementVO vo) {
        return CombineController.super.add(vo);
    }

    @PostMapping({"/get"})
    @ApiOperation("查看接口")
    @PreAuthorize("hasAnyRole('SYS_MENU_ELEMENT_ADMIN')")
    public SysMenuElementVO get(@RequestBody IdDTO vo) {
        return CombineController.super.get(vo);
    }

    @PostMapping({"/update"})
    @ApiOperation("更新接口")
    @PreAuthorize("hasAnyRole('SYS_MENU_ELEMENT_ADMIN')")
    public IdDTO update(@RequestBody SysMenuElementVO vo) {
        return CombineController.super.update(vo);
    }

    @PostMapping({"/delete"})
    @ApiOperation("删除接口")
    @PreAuthorize("hasAnyRole('SYS_MENU_ELEMENT_ADMIN')")
    public IdDTO delete(@RequestBody IdDTO vo) {
        return CombineController.super.delete(vo);
    }

    @PostMapping({"/deleteAll"})
    @ApiOperation("批量删除接口")
    @PreAuthorize("hasAnyRole('SYS_MENU_ELEMENT_ADMIN')")
    public IdsDTO deleteAll(@RequestBody IdsDTO ids) {
        return CombineController.super.deleteAll(ids);
    }
}
