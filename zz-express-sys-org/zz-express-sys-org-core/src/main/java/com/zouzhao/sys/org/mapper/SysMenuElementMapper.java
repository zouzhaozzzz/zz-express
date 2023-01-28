package com.zouzhao.sys.org.mapper;

import com.zouzhao.common.mapper.IMapper;
import com.zouzhao.sys.org.dto.SysMenuElementVO;
import com.zouzhao.sys.org.entity.SysMenuElement;
import com.zouzhao.sys.org.entity.SysRightRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-24
 */
public interface SysMenuElementMapper extends IMapper<SysMenuElement, SysMenuElementVO> {

    List<SysMenuElementVO> findVOListInRoles(@Param("list")List<SysRightRole> list,@Param("v")SysMenuElementVO vo);


}
