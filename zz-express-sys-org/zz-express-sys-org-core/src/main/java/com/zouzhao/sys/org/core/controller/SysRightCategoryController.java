package com.zouzhao.sys.org.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.controller.BaseController;
import com.zouzhao.common.controller.PageController;
import com.zouzhao.sys.org.api.ISysRightCategoryApi;
import com.zouzhao.sys.org.dto.SysRightCategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚超
 * @DATE: 2023-2-4
 */
@RestController
@RequestMapping("/data/sys-right/sysRightCategory")
@Api(
        tags = "权限类别"
)
public class SysRightCategoryController extends BaseController<ISysRightCategoryApi, SysRightCategoryVO> implements PageController<ISysRightCategoryApi, SysRightCategoryVO> {

    @PostMapping({"/page"})
    @ApiOperation("分页")
    public Page<SysRightCategoryVO> page(@RequestBody Page<SysRightCategoryVO> page) {
        return getApi().page(page);
    }
}
