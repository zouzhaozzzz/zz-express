package com.zouzhao.sys.org.client;

import com.zouzhao.sys.org.api.ISysRightRoleApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 姚超
 * @DATE: 2023-1-26
 */
@FeignClient(name="zz-sys-org",path="/api/sys-right/sysRightRole")
public interface SysRightRoleClient extends ISysRightRoleApi {
}
