package com.zouzhao.opt.manage.api;

import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
public interface IOptExportApi {

    @PostMapping("/exportSends")
    void exportSends();

    @PostMapping("/importSends")
    void importSends();
}
