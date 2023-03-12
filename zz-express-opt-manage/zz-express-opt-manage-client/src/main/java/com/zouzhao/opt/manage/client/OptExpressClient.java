package com.zouzhao.opt.manage.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@FeignClient(name = "zz-opt-manage", path="/api/opt-manage/optExpress")
public interface OptExpressClient {


}
