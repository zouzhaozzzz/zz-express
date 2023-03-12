package com.zouzhao.opt.file.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
@FeignClient(name = "zz-opt-file", path="/api/opt-file/optExport")
public interface OptExportClient  {

    //更新导入
    @PostMapping("/updateJustFinish")
    void updateJustFinish(@RequestBody String exportId);

    //更新导出
    @PostMapping("/updateExportJustFinish")
    void updateExportJustFinish(@RequestBody String exportId);
}
