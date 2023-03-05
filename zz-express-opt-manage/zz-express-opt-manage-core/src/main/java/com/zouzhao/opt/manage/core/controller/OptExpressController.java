package com.zouzhao.opt.manage.core.controller;

import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.controller.PageController;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import com.zouzhao.sys.org.client.SysMenuElementClient;
import com.zouzhao.sys.org.dto.SysMenuElementVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@RestController
@RequestMapping("/data/opt-manage/optExpress")
@Api(
        tags = "运单管理"
)
public class OptExpressController extends BaseController<IOptExpressApi, OptExpressVO> implements PageController<IOptExpressApi, OptExpressVO> {

    @Autowired
    private SysMenuElementClient sysMenuElementClient;

    @PostMapping("/test")
    public void testClient(){
        sysMenuElementClient.findAll(new SysMenuElementVO());
    }
}
