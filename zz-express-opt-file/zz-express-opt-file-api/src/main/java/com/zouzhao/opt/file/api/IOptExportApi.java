package com.zouzhao.opt.file.api;

import com.zouzhao.common.api.IPageApi;
import com.zouzhao.opt.file.dto.OptExportVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
public interface IOptExportApi extends IPageApi<OptExportVO> {

    @PostMapping("/exportSends")
    void exportSends();

    @PostMapping("/importSends")
    void importSends(String path,String exportId);

    @PostMapping("/updateJustFinish")
    void updateJustFinish(@RequestBody String exportId);
}
