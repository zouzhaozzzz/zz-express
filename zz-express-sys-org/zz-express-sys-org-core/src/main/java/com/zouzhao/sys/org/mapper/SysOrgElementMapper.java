package com.zouzhao.sys.org.mapper;

import com.zouzhao.common.mapper.IPageMapper;
import com.zouzhao.sys.org.entity.SysOrgElement;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-2-17
 */
public interface SysOrgElementMapper extends IPageMapper<SysOrgElement, SysOrgElementVO> {

    void batchUpdateStatus(@Param("ids") List<String> ids, @Param("status")boolean status);
}
