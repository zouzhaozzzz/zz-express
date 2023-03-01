package com.zouzhao.sys.org.client;

import com.zouzhao.sys.org.api.ISysOrgElementApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 姚超
 * @DATE: 2023-2-17
 */
@FeignClient(name = "zz-sys-org", path="/api/sys-org/sysOrgElement")
public interface SysOrgElementClient extends ISysOrgElementApi {
}
