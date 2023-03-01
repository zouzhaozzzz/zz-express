package com.zouzhao.sys.org.client;

import com.zouzhao.sys.org.api.ISysOrgAccountApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@FeignClient(name = "zz-sys-org", path="/api/sys-org/sysOrgAccount")
public interface SysOrgAccountClient extends ISysOrgAccountApi {

}
