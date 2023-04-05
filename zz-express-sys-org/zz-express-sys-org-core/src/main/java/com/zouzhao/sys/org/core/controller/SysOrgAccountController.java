package com.zouzhao.sys.org.core.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.sys.org.api.ISysMenuElementApi;
import com.zouzhao.sys.org.api.ISysOrgAccountApi;
import com.zouzhao.sys.org.core.security.utils.JsonUtils;
import com.zouzhao.sys.org.dto.SysMenuElementVO;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

    @Autowired
    private ISysMenuElementApi sysMenuElementApi;

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
        SysOrgAccountVO vo = new SysOrgAccountVO();
        vo.setOrgAccountDefPersonId(accountDefPersonId);
        vo.setOrgAccountPassword(accountPassword);
        super.getApi().changePasswordByDefPerson(vo);
        return new ResponseEntity<>("修改成功", HttpStatus.OK);
    }


    @PostMapping("/findByLoginName")
    @ApiOperation("根据用户名查询")
    @PreAuthorize("hasAnyRole('SYS_ORG_ACCOUNT_DEFAULT')")
    public String findByLoginName(@RequestBody SysOrgAccountVO user) {
        String loginName = user.getOrgAccountLoginName();
        if (StrUtil.isBlank(loginName)) throw new MyException("登录名为空或者有空字符");
        SysOrgAccountVO accountVO = getApi().findVOByLoginName(loginName);
        if (accountVO != null) return "false";
        return "success";
    }

    @PostMapping("/checkPath")
    @ApiOperation("检测是否拥有路由")
    @PreAuthorize("hasAnyRole('SYS_ORG_ACCOUNT_DEFAULT')")
    public void checkPath(@RequestBody Map<String,String> path, HttpServletResponse response) throws IOException {
        if (StrUtil.isEmpty(path.get("path"))) throw new MyException("没有传入路由路径");
        SysMenuElementVO vo = new SysMenuElementVO();
        vo.setMenuElementPath(path.get("path"));
        vo.setMenuElementStatus(true);
        //是否有菜单的权限
        List<SysMenuElementVO> list = sysMenuElementApi.listInRoles(vo);
        if (list == null || list.size() < 1) {
            JsonUtils.writeToJson(response, HttpStatus.FORBIDDEN.value());
        }else JsonUtils.writeToJson(response, "success");
    }
}

