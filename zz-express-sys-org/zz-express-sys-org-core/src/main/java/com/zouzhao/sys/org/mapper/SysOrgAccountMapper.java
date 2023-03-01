package com.zouzhao.sys.org.mapper;

import com.zouzhao.common.mapper.IMapper;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import com.zouzhao.sys.org.entity.SysOrgAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
public interface SysOrgAccountMapper extends IMapper<SysOrgAccount,SysOrgAccountVO> {

    SysOrgAccountVO findVOByDefPersonId(String id);

    List<String> getIdsByDefPersonIds(@Param("list") List<String> elementIds);

    void changePasswordByDefPerson(@Param("accountDefPersonId") String accountDefPersonId, @Param("password") String ps);
}
