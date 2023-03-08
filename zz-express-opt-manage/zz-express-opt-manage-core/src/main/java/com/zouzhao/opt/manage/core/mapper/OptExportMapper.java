package com.zouzhao.opt.manage.core.mapper;

import com.zouzhao.common.core.mapper.IPageMapper;
import com.zouzhao.opt.manage.core.entity.OptExport;
import com.zouzhao.opt.manage.dto.OptExportVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-3-7
 */
public interface OptExportMapper extends IPageMapper<OptExport, OptExportVO> {

    void updateFinishTimeById(@Param("id") String exportId,@Param("finishTime") Date date);
}
