package com.zouzhao.express.opt.manage.core.controller;

import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.PageController;
import com.zouzhao.express.opt.manage.api.IOptExpressApi;
import com.zouzhao.express.opt.manage.core.entity.OptExpressVO;
import io.swagger.annotations.Api;
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

}
