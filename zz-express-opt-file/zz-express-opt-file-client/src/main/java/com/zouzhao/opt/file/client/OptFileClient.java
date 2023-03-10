package com.zouzhao.opt.file.client;

import com.zouzhao.opt.file.api.IOptFileApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 姚超
 * @DATE: 2023-3-9
 */
@FeignClient(name = "zz-opt-file", path="/api/opt-file/optFile")
public interface OptFileClient extends IOptFileApi {
}
