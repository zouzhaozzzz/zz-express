package com.zouzhao.sys.org.mapper;

import com.zouzhao.common.mapper.IMapper;
import com.zouzhao.sys.org.dto.SysMenuElementVO;
import com.zouzhao.sys.org.entity.SysMenuElement;
import com.zouzhao.sys.org.entity.SysRightRole;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 姚超
 * @DATE: 2023-1-24
 */
public interface SysMenuElementMapper extends IMapper<SysMenuElement, SysMenuElementVO> {

    List<SysMenuElementVO> findVOListInRoles(@Param("list")List<SysRightRole> list,@Param("v")SysMenuElementVO vo);

    //菜单权限中间表start
    void insertRoleRela(@Param("id") String menuElementId, @Param("list") List<SysRightRole> sysRightRoles);

    @MapKey("roleId")
    Map<String, Map<Object,Object>> findRoleRela(@Param("id")String menuElementId);

    void deleteRoleRela(@Param("id")String menuElementId, @Param("list")ArrayList<String> strings);

    void deleteAllRoleRela(String id);

    //菜单权限中间表end
}
