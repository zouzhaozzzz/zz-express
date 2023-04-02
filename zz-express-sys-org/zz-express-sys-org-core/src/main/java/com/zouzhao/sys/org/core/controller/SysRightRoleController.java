package com.zouzhao.sys.org.core.controller;

import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.controller.ListController;
import com.zouzhao.sys.org.api.ISysRightRoleApi;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
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
 * @DATE: 2023-1-26
 */
@RestController
@RequestMapping("/data/sys-right/sysRightRole")
@Api(
        tags = "权限"
)
@PreAuthorize("hasAnyRole('SYS_RIGHT_ROLE_DEFAULT')")
public class SysRightRoleController extends BaseController<ISysRightRoleApi, SysRightRoleVO> implements ListController<ISysRightRoleApi, SysRightRoleVO> {

    @PostMapping("/allModule")
    @PreAuthorize("hasAnyRole('SYS_RIGHT_ROLE_LIST')")
    public List<String> allModule(){
        return getApi().allModule();
    }

    @PostMapping({"/list"})
    @ApiOperation("列表查询接口")
    @PreAuthorize("hasAnyRole('SYS_RIGHT_ROLE_LIST')")
    public List<SysRightRoleVO> list(@RequestBody SysRightRoleVO request) {
        return this.getApi().findAll(request);    }
}
