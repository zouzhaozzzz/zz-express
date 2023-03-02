package com.zouzhao.sys.org.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.PageController;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.sys.org.api.ISysOrgElementApi;
import com.zouzhao.sys.org.api.ISysRightGroupApi;
import com.zouzhao.sys.org.dto.SysRightGroupVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
@RestController
@RequestMapping("/data/sys-right/sysRightGroup")
@Api(
        tags = "角色"
)
@PreAuthorize("hasAnyRole('SYS_RIGHT_GROUP_DEFAULT')")
public class SysRightGroupController extends BaseController<ISysRightGroupApi, SysRightGroupVO> implements PageController<ISysRightGroupApi, SysRightGroupVO> {

    @Autowired
    private ISysOrgElementApi sysOrgElementApi;

    @PostMapping("/listPersonId")
    @ApiOperation("查询角色附带分配的用户id")
    @PreAuthorize("hasAnyRole('SYS_RIGHT_GROUP_LIST')")
    public SysRightGroupVO listPersonId(@RequestBody SysRightGroupVO vo) {
        List<String> personIds = getApi().listPersonIdByAccount(IdDTO.of(vo.getRightGroupId()));
        vo.setElementIds(personIds);
        return vo;
    }

    @PostMapping("/allotToAccount")
    @ApiOperation("分配角色给账号")
    @PreAuthorize("hasAnyRole('SYS_RIGHT_GROUP_ALLOT')")
    public void allotToAccount(@RequestBody SysRightGroupVO vo) {
        getApi().allotToAccount(vo);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_GROUP_INSERT')")
    public IdDTO add(@RequestBody SysRightGroupVO vo) {
        return PageController.super.add(vo);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_GROUP_LIST')")
    public SysRightGroupVO get(@RequestBody IdDTO vo) {
        return PageController.super.get(vo);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_GROUP_UPDATE')")
    public IdDTO update(@RequestBody SysRightGroupVO vo) {
        return PageController.super.update(vo);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_GROUP_DELETE')")
    public IdDTO delete(@RequestBody IdDTO vo) {
        return PageController.super.delete(vo);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_GROUP_DELETE')")
    public IdsDTO deleteAll(@RequestBody IdsDTO ids) {
        return PageController.super.deleteAll(ids);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_GROUP_LIST')")
    public List<SysRightGroupVO> list(@RequestBody SysRightGroupVO request) {
        return PageController.super.list(request);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_GROUP_LIST')")
    public Page<SysRightGroupVO> page(@RequestBody Page<SysRightGroupVO> page) {
        return PageController.super.page(page);
    }
}
