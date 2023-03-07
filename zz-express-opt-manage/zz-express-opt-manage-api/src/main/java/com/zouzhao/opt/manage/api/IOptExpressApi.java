package com.zouzhao.opt.manage.api;

import com.zouzhao.common.api.IPageApi;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
public interface IOptExpressApi extends IPageApi<OptExpressVO> {

    @PostMapping("/batchSave")
    void batchSave(List<OptExpressVO> list);
}
