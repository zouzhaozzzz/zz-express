package com.zouzhao.opt.manage.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.api.IPageApi;
import com.zouzhao.opt.file.dto.OptExportConditionVO;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import org.apache.ibatis.session.ResultHandler;
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

    @PostMapping("/pageQueryByCondition")
    void pageQueryByCondition(OptExportConditionVO vo, ResultHandler<OptExpressVO> resultHandler);

    @PostMapping("/updateStatusBatch")
    void updateStatusBatch(List<String> ids,Integer status);

    @PostMapping("/pagePlus")
    Page<OptExpressVO> pagePlus(Page<OptExpressVO> page, List<SysOrgElementVO> orgList);

    @PostMapping("/refreshExport")
    String refreshExport(Boolean initFlag);

    @PostMapping("/countExpressNum")
    int countExpressNum(OptExpressVO vo);
}
