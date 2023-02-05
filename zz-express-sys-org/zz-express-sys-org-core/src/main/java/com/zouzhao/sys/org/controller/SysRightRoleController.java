package com.zouzhao.sys.org.controller;

import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.CombineController;
import com.zouzhao.sys.org.api.ISysRightRoleApi;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-26
 */
@RestController
@RequestMapping("/data/sys-right/sysRightRole")
@Api(
        tags = "权限"
)
public class SysRightRoleController extends BaseController<ISysRightRoleApi, SysRightRoleVO> implements CombineController<ISysRightRoleApi, SysRightRoleVO> {

    @PostMapping("/allModule")
    public List<String> allModule(){
        return getApi().allModule();
    }
}
