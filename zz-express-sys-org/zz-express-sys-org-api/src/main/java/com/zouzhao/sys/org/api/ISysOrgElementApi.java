package com.zouzhao.sys.org.api;

import com.zouzhao.common.api.IPageApi;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-2-17
 */
public interface ISysOrgElementApi extends IPageApi<SysOrgElementVO> {

    @PostMapping({"/treeData"})
    List<SysOrgElementVO> treeData(@RequestBody SysOrgElementVO request);

    @PostMapping("/disableAll")
    @ApiOperation("批量停用")
    void disableAll(IdsDTO idsDTO);

    @PostMapping("/countOrg")
    int countOrg();

    @PostMapping("/countPerson")
    int countPerson();

    @PostMapping("/listInRoles")
    List<SysOrgElementVO> listInRoles(@RequestBody SysOrgElementVO request);

    @PostMapping("/findChildOrgById")
    List<SysOrgElementVO> findChildOrgById(@RequestBody SysOrgElementVO request);

    @PostMapping("/findByLoginName")
    SysOrgElementVO findByLoginName(@RequestBody SysOrgElementVO request);

    @PostMapping("/findAllParentOrg")
    List<SysOrgElementVO> findAllParentOrg();

    @PostMapping("/findVOByLoginName")
    SysOrgElementVO findVOByLoginName(@RequestBody SysOrgElementVO request);
}
