package com.zouzhao.sys.org.mapper;

import com.zouzhao.common.mapper.IPageMapper;
import com.zouzhao.sys.org.dto.SysRightGroupVO;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import com.zouzhao.sys.org.entity.SysRightGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface SysRightGroupMapper extends IPageMapper<SysRightGroup,SysRightGroupVO> {


    void addRoleRela(@Param("list") List<SysRightRoleVO> list, @Param("id") String id);

    void removeRela(String id);


    void removeRelaList(@Param("list")List<String> ids);
}
