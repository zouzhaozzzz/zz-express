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


    @PostMapping("/importSends")
    void importSends(String path,String exportId);

    //更新导入
    @PostMapping("/updateJustFinish")
    void updateJustFinish(@RequestBody String exportId);

    //更新导出
    @PostMapping("/updateExportJustFinish")
    void updateExportJustFinish(@RequestBody String exportId);
}
