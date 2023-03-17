package com.zouzhao.sys.org.core.mapper;

import com.zouzhao.common.core.mapper.IPageMapper;
import com.zouzhao.sys.org.core.entity.SysOrgElement;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-2-17
 */
public interface SysOrgElementMapper extends IPageMapper<SysOrgElement, SysOrgElementVO> {

    void batchUpdateStatus(@Param("ids") List<String> ids, @Param("status")boolean status);

    int countOrg();

    int countPerson();

    SysOrgElementVO findByLoginName(@Param("v") SysOrgElementVO request);

    List<SysOrgElementVO> findChildOrgById(@Param("v") SysOrgElementVO org);

    List<SysOrgElementVO> findVOListInOrgs(@Param("v") SysOrgElementVO org,@Param("list")List<SysOrgElementVO> data);

    List<SysOrgElementVO> findAllParentOrg();
}
