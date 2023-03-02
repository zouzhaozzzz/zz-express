package com.zouzhao.sys.org.core.controller;

import cn.hutool.core.util.ObjectUtil;
import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.exception.MyException;
import com.zouzhao.sys.org.api.ISysOrgAccountApi;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@RestController
@RequestMapping("/data/sys-org/sysOrgAccount")
@Api(
        tags = "用户账号"
)
public class SysOrgAccountController extends BaseController<ISysOrgAccountApi, SysOrgAccountVO> {


    @PostMapping("/checkLogin")
    @ApiOperation("登陆")
    public String checkLogin(@RequestBody SysOrgAccountVO user) {
        return super.getApi().checkLogin(user);
    }

    @PostMapping("/layout")
    @ApiOperation("退出登陆")
    public String layout(@RequestBody SysOrgAccountVO user) {
        return super.getApi().layout(user);
    }

    @PostMapping("/update")
    @ApiOperation("修改密码")
    @PreAuthorize("hasAnyRole('SYS_ORG_ACCOUNT_DEFAULT')")
    public ResponseEntity<?> update(@RequestBody Map<String, String> info) {
        if (ObjectUtil.isEmpty(info)) throw new MyException("未传入信息");
        String accountDefPersonId = info.get("orgAccountDefPersonId");
        String accountPassword = info.get("orgAccountPassword");
        if (ObjectUtil.isEmpty(accountDefPersonId) && ObjectUtil.isEmpty(accountPassword))
            throw new MyException("信息不全");
        //修改密码
        super.getApi().changePasswordByDefPerson(accountDefPersonId, accountPassword);
        return new ResponseEntity<>("修改成功", HttpStatus.OK);
    }


}

