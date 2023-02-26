package com.zouzhao.sys.org.mapper;

import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.mapper.IMapper;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import com.zouzhao.sys.org.entity.SysOrgAccount;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
public interface SysOrgAccountMapper extends IMapper<SysOrgAccount,SysOrgAccountVO> {


    IdDTO addOrUpdate(SysOrgAccountVO vo);
}
