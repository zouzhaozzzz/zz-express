package com.zouzhao.opt.file.core.mapper;

import com.zouzhao.common.core.mapper.IPageMapper;
import com.zouzhao.opt.file.core.entity.OptExport;
import com.zouzhao.opt.file.dto.OptExportVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-3-7
 */
public interface OptExportMapper extends IPageMapper<OptExport, OptExportVO> {

    void updateJustFinish(@Param("id") String exportId, @Param("finishTime") Date date,@Param("msg") String msg);

    void updateExportJustFinish(@Param("id")String exportId,  @Param("finishTime")Date date, @Param("msg")String msg, @Param("fileId")String exportFileId);
}
