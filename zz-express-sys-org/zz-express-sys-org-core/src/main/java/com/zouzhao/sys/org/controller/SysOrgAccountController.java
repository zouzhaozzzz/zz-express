package com.zouzhao.sys.org.controller;

import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.CombineController;
import com.zouzhao.common.dto.Response;
import com.zouzhao.sys.org.api.ISysOrgAccountApi;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@RestController
@RequestMapping("/data/sys-org/sysOrgAccount")
@Api(
        tags = "用户账号"
)
public class SysOrgAccountController extends BaseController<ISysOrgAccountApi, SysOrgAccountVO> implements CombineController<ISysOrgAccountApi, SysOrgAccountVO> {


    @PostMapping("/checkLogin")
    @ApiOperation("登陆")
    public Response<String> checkLogin(@RequestBody SysOrgAccountVO user) {
       return super.getApi().checkLogin(user);
    }

    @PostMapping("/layout")
    @ApiOperation("退出登陆")
    public Response<String> layout(@RequestBody SysOrgAccountVO user){
        return  super.getApi().layout(user);
    }



}

