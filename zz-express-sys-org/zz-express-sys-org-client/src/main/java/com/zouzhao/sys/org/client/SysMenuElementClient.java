package com.zouzhao.sys.org.client;

import com.zouzhao.sys.org.api.ISysMenuElementApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 姚超
 * @DATE: 2023-1-24
 */
@FeignClient(name = "zz-sys-org", path="/api/sys-org/sysMenuElement")
public interface SysMenuElementClient extends ISysMenuElementApi {

}
