package com.zouzhao.sys.org.api;

import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.Response;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
public interface ISysOrgAccountApi extends IApi<SysOrgAccountVO> {

    @PostMapping("/checkLogin")
    Response<String> checkLogin(SysOrgAccountVO user);

    @PostMapping("/layout")
    Response<String> layout(SysOrgAccountVO user);


}
