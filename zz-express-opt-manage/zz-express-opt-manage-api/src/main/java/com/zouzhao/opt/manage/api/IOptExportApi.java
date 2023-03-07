package com.zouzhao.opt.manage.api;

import com.zouzhao.common.api.IPageApi;
import com.zouzhao.opt.manage.dto.OptExportVO;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
public interface IOptExportApi extends IPageApi<OptExportVO> {

    @PostMapping("/exportSends")
    void exportSends();

    @PostMapping("/importSends")
    void importSends();
}
