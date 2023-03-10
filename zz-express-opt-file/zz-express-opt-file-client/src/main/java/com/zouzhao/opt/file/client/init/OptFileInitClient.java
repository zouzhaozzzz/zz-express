package com.zouzhao.opt.file.client.init;

import com.zouzhao.opt.file.api.init.IOptFileInit;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
@FeignClient(name = "zz-opt-file", path="/api/opt-file/optFileInit")
public interface OptFileInitClient extends IOptFileInit {
}
