package com.zouzhao.sys.org.service;

import com.zouzhao.common.service.BaseServiceImpl;
import com.zouzhao.sys.org.api.ISysRightRoleApi;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import com.zouzhao.sys.org.entity.SysRightRole;
import com.zouzhao.sys.org.mapper.SysRightRoleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-26
 */
@Service
@RestController
@RequestMapping("/api/sys-org/sysRightRole")
public class SysRightRoleService extends BaseServiceImpl<SysRightRoleMapper, SysRightRole, SysRightRoleVO> implements ISysRightRoleApi {


    @Override
    public SysRightRole voToEntity(SysRightRoleVO vo) {
        SysRightRole sysRightRole = new SysRightRole();
        BeanUtils.copyProperties(vo,sysRightRole);
        return sysRightRole;
    }

    @Override
    public List<String> allModule() {
        return getMapper().getAllModules();
    }
}
