package com.zouzhao.sys.org.controller;

import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.CombineController;
import com.zouzhao.sys.org.api.ISysRightGroupApi;
import com.zouzhao.sys.org.dto.SysRightGroupVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
@RestController
@RequestMapping("/data/sys-right/sysRightGroup")
@Api(
        tags = "角色"
)
public class SysRightGroupController extends BaseController<ISysRightGroupApi, SysRightGroupVO> implements CombineController<ISysRightGroupApi, SysRightGroupVO> {



}
