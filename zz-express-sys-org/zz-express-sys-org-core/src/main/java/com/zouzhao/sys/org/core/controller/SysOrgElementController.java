package com.zouzhao.sys.org.core.controller;

import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.PageController;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.sys.org.api.ISysOrgAccountApi;
import com.zouzhao.sys.org.api.ISysOrgElementApi;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private ISysOrgAccountApi sysOrgAccountService;

    @PostMapping({"/treeData"})
    @ApiOperation("列表查询接口")
    public List<SysOrgElementVO> treeData(@RequestBody SysOrgElementVO request) {
        return getApi().treeData(request);
    }

    @PostMapping({"/get"})
    @ApiOperation("查看接口")
    public SysOrgElementVO get(@RequestBody IdDTO vo) {
        SysOrgElementVO sysOrgElementVO = PageController.super.get(vo);
        //如果为人员，查询账号信息
        if(sysOrgElementVO.getOrgElementType() == 1 && !StringUtils.isBlank(sysOrgElementVO.getOrgElementId()) ){
        SysOrgAccountVO sysOrgAccountVO=sysOrgAccountService.findVOByDefPersonId(IdDTO.of(sysOrgElementVO.getOrgElementId()));
        if(sysOrgAccountVO != null)sysOrgElementVO.setOrgElementLoginName(sysOrgAccountVO.getOrgAccountLoginName());}
        return sysOrgElementVO;
    }

    @PostMapping("/disableAll")
    @ApiOperation("批量停用")
    public ResponseEntity<String> disableAll(@RequestBody IdsDTO idsDTO){
        getApi().disableAll(idsDTO);
        return new ResponseEntity<String>("停用成功", HttpStatus.OK);
    }
}
