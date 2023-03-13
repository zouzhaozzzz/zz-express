package com.zouzhao.opt.manage.core.mapper;

import com.zouzhao.common.core.mapper.IPageMapper;
import com.zouzhao.opt.file.dto.OptExportConditionVO;
import com.zouzhao.opt.manage.core.entity.OptExpress;
import com.zouzhao.opt.manage.dto.OptExpressMonthFeeVO;
import com.zouzhao.opt.manage.dto.OptExpressMonthNumVO;
import com.zouzhao.opt.manage.dto.OptExpressNumVO;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */

public interface OptExpressMapper extends IPageMapper<OptExpress, OptExpressVO> {

    Integer isExistById(@Param("id") String expressId);

    void updateStatusBatch(@Param("list")List<String> ids, @Param("status") Integer status);

    int countByStatus(@Param("status")Integer status);

    List<OptExpressNumVO> countByConsigneeProvinces();

    List<OptExpressNumVO> countBySendProvinces();

    List<OptExpressMonthNumVO> countByBounce();

    List<OptExpressMonthNumVO> countByQuestion();

    List<OptExpressMonthNumVO> countExpressNumByMonth();

    List<OptExpressMonthFeeVO> countTotalCostByMonth();

    List<OptExpressMonthFeeVO> countSendFineByMonth();

    List<OptExpressMonthFeeVO> countFreightByMonth();

    List<OptExpressMonthFeeVO> countPremiumByMonth();

    int countExpressNum(@Param("v") OptExpressVO vo);
    //导出数据流

    void pageQueryByCondition(@Param("v") OptExportConditionVO vo, ResultHandler<OptExpressVO> handler);


    //分页plus start
    long findCount(@Param("v") OptExpressVO e);

    List<OptExpressVO> pagePlus(@Param("current") long current, @Param("size") long size, @Param("v") OptExpressVO optExpressVO);
    //分页plus end
}
