package com.zouzhao.sys.org.core.mapper;

import com.zouzhao.common.core.mapper.IMapper;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import com.zouzhao.sys.org.core.entity.SysRightRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface SysRightRoleMapper extends IMapper<SysRightRole,SysRightRoleVO> {

    void saveOrUpdateBatchByName(@Param("list") List<SysRightRole> role);

    List<String> getAllModules();

    void deleteWithRelation(@Param("rightRoleCodeList") List<String> rightRoleCodeList);
}
