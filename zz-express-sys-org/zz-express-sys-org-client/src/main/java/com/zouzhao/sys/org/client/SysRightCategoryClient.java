package com.zouzhao.sys.org.client;

import com.zouzhao.sys.org.api.ISysRightCategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 姚超
 * @DATE: 2023-2-4
 */
@FeignClient(name = "zz-sys-org", path="/api/sys-right/sysRightCategory")
public interface SysRightCategoryClient extends ISysRightCategoryApi {


}
