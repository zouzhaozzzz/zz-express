package com.zouzhao.sys.org.mapper;

import com.zouzhao.common.mapper.IMapper;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import com.zouzhao.sys.org.entity.SysRightRole;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface SysRightRoleMapper extends IMapper<SysRightRole,SysRightRoleVO> {

    List<SysRightRole> findList(SysRightRole sysRightRole);

    List<SysRightRoleVO> findVOList (SysRightRole sysRightRole);

    SysRightRole findById(String rightRoleId);

    SysRightRoleVO findVOById(String rightRoleId);




}
