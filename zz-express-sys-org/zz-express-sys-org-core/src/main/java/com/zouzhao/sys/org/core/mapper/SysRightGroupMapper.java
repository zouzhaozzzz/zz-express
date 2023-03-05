package com.zouzhao.sys.org.core.mapper;

import com.zouzhao.common.core.mapper.IPageMapper;
import com.zouzhao.sys.org.dto.SysRightGroupVO;
import com.zouzhao.sys.org.core.entity.SysRightGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface SysRightGroupMapper extends IPageMapper<SysRightGroup,SysRightGroupVO> {

    //sys_right_gr_rela 角色-权限 start
    void addRoleRela(@Param("list") List<String> list, @Param("id") String id);

    void removeRela(String id);

    //删除sys_right_gr_rela中间表right_group_id in ids
    void removeRelaList(@Param("list")List<String> ids);
    //sys_right_gr_rela 角色-权限 end


    //sys_right_go_rela 角色-账号 start
    List<String> listPersonIdByAccount(String id);

    List<String> listAccountIdsByGroupId(String rightGroupId);

    void insertAccountRela(@Param("id") String id,@Param("list") List<String> ids);

    void deleteAccountRela(@Param("id") String id,@Param("list") List<String> ids);

    void deleteAccountRelaById(String id);

    void deleteAccountRelaByIds(@Param("list") List<String> ids);

    //sys_right_go_rela 角色-账号 end
}
