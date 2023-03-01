package com.zouzhao.sys.org.core.controller;

import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.PageController;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.sys.org.api.ISysOrgElementApi;
import com.zouzhao.sys.org.api.ISysRightGroupApi;
import com.zouzhao.sys.org.dto.SysRightGroupVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SysRightGroupController extends BaseController<ISysRightGroupApi, SysRightGroupVO> implements PageController<ISysRightGroupApi, SysRightGroupVO> {

    @Autowired
    private ISysOrgElementApi sysOrgElementApi;

    @PostMapping("/listPersonId")
    @ApiOperation("查询角色附带分配的用户id")
    public SysRightGroupVO listPersonId(@RequestBody SysRightGroupVO vo) {
        List<String> personIds = getApi().listPersonIdByAccount(IdDTO.of(vo.getRightGroupId()));
        vo.setElementIds(personIds);
        return vo;
    }

    @PostMapping("/allotToAccount")
    @ApiOperation("分配角色给账号")
    public void allotToAccount(@RequestBody SysRightGroupVO vo) {
         getApi().allotToAccount(vo);
    }

}
