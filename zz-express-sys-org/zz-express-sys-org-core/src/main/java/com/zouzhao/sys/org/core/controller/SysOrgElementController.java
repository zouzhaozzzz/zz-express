package com.zouzhao.sys.org.core.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.sys.org.api.ISysOrgAccountApi;
import com.zouzhao.sys.org.api.ISysOrgElementApi;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 姚超
 * @DATE: 2023-2-17
 */
@RestController
@RequestMapping("/data/sys-org/sysOrgElement")
@Api(
        tags = "组织元素"
)
@PreAuthorize("hasAnyRole('SYS_ORG_ELEMENT_DEFAULT','SYS_ORG_ELEMENT_ADMIN')")
public class SysOrgElementController extends BaseController<ISysOrgElementApi, SysOrgElementVO> {

    @Autowired
    private ISysOrgAccountApi sysOrgAccountService;

    @PostMapping({"/treeData"})
    @ApiOperation("列表查询接口")
    public List<SysOrgElementVO> treeData(@RequestBody SysOrgElementVO request) {
        return getApi().treeData(request);
    }

    @PostMapping({"/get"})
    @ApiOperation("查看接口")
    @PreAuthorize("hasAnyRole('SYS_ORG_ELEMENT_LIST','SYS_ORG_ELEMENT_ADMIN')")
    public SysOrgElementVO get(@RequestBody IdDTO vo) {
        SysOrgElementVO sysOrgElementVO = getApi().findVOById(vo);
        //如果为人员，查询账号信息
        if (sysOrgElementVO.getOrgElementType() == 1 && !StrUtil.isBlank(sysOrgElementVO.getOrgElementId())) {
            SysOrgAccountVO sysOrgAccountVO = sysOrgAccountService.findVOByDefPersonId(IdDTO.of(sysOrgElementVO.getOrgElementId()));
            if (sysOrgAccountVO != null)
                sysOrgElementVO.setOrgElementLoginName(sysOrgAccountVO.getOrgAccountLoginName());
        }
        return sysOrgElementVO;
    }

    @PostMapping("/disableAll")
    @ApiOperation("批量停用")
    @PreAuthorize("hasAnyRole('SYS_ORG_ELEMENT_UPDATE','SYS_ORG_ELEMENT_ADMIN')")
    public ResponseEntity<String> disableAll(@RequestBody IdsDTO idsDTO) {
        getApi().disableAll(idsDTO);
        return new ResponseEntity<String>("停用成功", HttpStatus.OK);
    }

    @PostMapping({"/add"})
    @ApiOperation("新增接口")
    @PreAuthorize("hasAnyRole('SYS_ORG_ELEMENT_INSERT','SYS_ORG_ELEMENT_ADMIN')")
    public IdDTO add(@RequestBody SysOrgElementVO vo) {
        return getApi().add(vo);
    }

    @PostMapping({"/update"})
    @ApiOperation("更新接口")
    @PreAuthorize("hasAnyRole('SYS_ORG_ELEMENT_UPDATE','SYS_ORG_ELEMENT_ADMIN')")
    public IdDTO update(@RequestBody SysOrgElementVO vo) {
        return getApi().update(vo);
    }


    @PostMapping({"/list"})
    @ApiOperation("列表查询接口")
    @PreAuthorize("hasAnyRole('SYS_ORG_ELEMENT_ADMIN')")
    public List<SysOrgElementVO> list(@RequestBody SysOrgElementVO request) {
        return getApi().findAll(request);
    }

    @PostMapping({"/page"})
    @ApiOperation("分页")
    @PreAuthorize("hasAnyRole('SYS_ORG_ELEMENT_LIST','SYS_ORG_ELEMENT_ADMIN')")
    public Page<SysOrgElementVO> page(@RequestBody Page<SysOrgElementVO> page) {
        return getApi().page(page);
    }

    @PreAuthorize("hasAnyRole('SYS_ORG_ELEMENT_ADMIN')")
    @ApiOperation("统计组织架构和人员")
    @PostMapping("/countOrg")
    public Map<String,Object> refreshOrg() {
        Map<String,Object> result=new HashMap<>();
        int orgNum = getApi().countOrg();
        int PersonNum = getApi().countPerson();
        result.put("org",orgNum);
        result.put("person",PersonNum);
        return result;
    }


}
