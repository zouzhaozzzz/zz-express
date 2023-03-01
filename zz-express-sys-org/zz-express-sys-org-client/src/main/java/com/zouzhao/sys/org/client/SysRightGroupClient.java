package com.zouzhao.sys.org.client;

import com.zouzhao.sys.org.api.ISysRightGroupApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
@FeignClient(name="zz-sys-org",path="/api/sys-right/sysRightGroup")
public interface SysRightGroupClient extends ISysRightGroupApi {

}
