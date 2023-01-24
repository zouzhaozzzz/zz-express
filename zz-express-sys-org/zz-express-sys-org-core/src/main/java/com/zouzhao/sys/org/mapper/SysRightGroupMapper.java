package com.zouzhao.sys.org.mapper;

import com.zouzhao.common.mapper.IMapper;
import com.zouzhao.sys.org.dto.SysRightGroupVO;
import com.zouzhao.sys.org.entity.SysRightGroup;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface SysRightGroupMapper extends IMapper<SysRightGroup,SysRightGroupVO> {

    List<SysRightGroup> findList(SysRightGroup sysRightGroup);

    List<SysRightGroupVO> findVOList(SysRightGroup sysRightGroup);

    SysRightGroup findById(String rightGroupId);

    SysRightGroupVO findVOById(String rightGroupId);
}
