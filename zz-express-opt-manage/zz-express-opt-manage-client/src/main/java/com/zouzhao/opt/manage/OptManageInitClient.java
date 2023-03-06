package com.zouzhao.opt.manage;

import com.zouzhao.opt.manage.api.init.IOptManageInit;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
@FeignClient(name = "zz-opt-manage", path="/api/opt-manage/optManageInit")
public interface OptManageInitClient extends IOptManageInit {
}
