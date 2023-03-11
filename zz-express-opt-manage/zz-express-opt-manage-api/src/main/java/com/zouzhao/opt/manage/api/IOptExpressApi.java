package com.zouzhao.opt.manage.api;

import com.zouzhao.common.api.IPageApi;
import com.zouzhao.opt.manage.dto.OptExpressMonthFeeVO;
import com.zouzhao.opt.manage.dto.OptExpressMonthNumVO;
import com.zouzhao.opt.manage.dto.OptExpressProvinceVO;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
public interface IOptExpressApi extends IPageApi<OptExpressVO> {

    @PostMapping("/batchSave")
    //如果存在相同运单号不新增
    void batchSave(String exportId, List<OptExpressVO> list);

    @PostMapping("/updateStatusBatch")
    void updateStatusBatch(List<String> ids,Integer status);

    @PostMapping("/countByStatus")
    int countByStatus(Integer status);

    @PostMapping("/countConsignByProvinces")
    List<OptExpressProvinceVO> countConsignByProvinces();

    @PostMapping("/countSendByProvinces")
    List<OptExpressProvinceVO> countSendByProvinces();

    @PostMapping("/countBounceByMonth")
    List<OptExpressMonthNumVO> countBounceByMonth();

    @PostMapping("/countQuestionByMonth")
    List<OptExpressMonthNumVO> countQuestionByMonth();

    @PostMapping("/countTotalCostByMonth")
    List<OptExpressMonthFeeVO> countTotalCostByMonth();

    @PostMapping("/countSendFineByMonth")
    List<OptExpressMonthFeeVO> countSendFineByMonth();

    @PostMapping("/countFreightByMonth")
    List<OptExpressMonthFeeVO> countFreightByMonth();

    @PostMapping("/countPremiumByMonth")
    List<OptExpressMonthFeeVO> countPremiumByMonth();
}
