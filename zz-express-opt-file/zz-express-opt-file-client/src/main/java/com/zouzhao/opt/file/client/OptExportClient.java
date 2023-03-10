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

    @PostMapping("/updateJustFinish")
    void updateJustFinish(@RequestBody String exportId);
}
