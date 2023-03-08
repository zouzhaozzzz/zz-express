package com.zouzhao.opt.manage.core.mapper;

import com.zouzhao.common.core.mapper.IPageMapper;
import com.zouzhao.opt.manage.core.entity.OptExpress;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */

public interface OptExpressMapper extends IPageMapper<OptExpress, OptExpressVO> {

    Integer isExistById(@Param("id") String expressId);
}
