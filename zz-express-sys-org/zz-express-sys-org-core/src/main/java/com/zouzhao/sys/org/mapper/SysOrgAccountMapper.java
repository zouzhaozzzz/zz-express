package com.zouzhao.sys.org.mapper;

import com.zouzhao.common.mapper.IMapper;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import com.zouzhao.sys.org.entity.SysOrgAccount;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
public interface SysOrgAccountMapper extends IMapper<SysOrgAccount,SysOrgAccountVO> {

   List<SysOrgAccount> findList(SysOrgAccount sysOrgAccount);

   List<SysOrgAccountVO> findVOList(SysOrgAccount sysOrgAccount);

   SysOrgAccount findById(String OrgAccountId);

   SysOrgAccountVO findVOById(String OrgAccountId);

}
