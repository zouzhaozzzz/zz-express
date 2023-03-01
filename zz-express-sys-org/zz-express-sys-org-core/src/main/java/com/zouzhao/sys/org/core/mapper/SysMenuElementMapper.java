package com.zouzhao.sys.org.core.mapper;

import com.zouzhao.common.mapper.IMapper;
import com.zouzhao.sys.org.dto.SysMenuElementVO;
import com.zouzhao.sys.org.core.entity.SysMenuElement;
import com.zouzhao.sys.org.core.entity.SysRightRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-24
 */
public interface SysMenuElementMapper extends IMapper<SysMenuElement, SysMenuElementVO> {

    List<SysMenuElementVO> findVOListInRoles(@Param("list")List<SysRightRole> list,@Param("v")SysMenuElementVO vo);

    //菜单权限中间表start
    void insertRoleRela(@Param("id") String menuElementId, @Param("list") List<String> ids);


    List<String> findRoleRela(@Param("id")String menuElementId);

    void deleteRoleRela(@Param("id")String menuElementId, @Param("list")List<String> ids);

    void deleteAllRoleRela(String id);

    //菜单权限中间表end
}
