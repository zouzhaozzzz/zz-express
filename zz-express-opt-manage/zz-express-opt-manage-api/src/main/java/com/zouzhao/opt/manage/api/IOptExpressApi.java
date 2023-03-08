package com.zouzhao.opt.manage.api;

import com.zouzhao.common.api.IPageApi;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
public interface IOptExpressApi extends IPageApi<OptExpressVO> {

    @PostMapping("/batchSave")
    //如果存在相同运单号不新增
    void batchSave(@RequestBody List<OptExpressVO> list);
}
