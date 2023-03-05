package com.zouzhao.sys.org.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.controller.PageController;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.sys.org.api.ISysRightCategoryApi;
import com.zouzhao.sys.org.dto.SysRightCategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasAnyRole('SYS_RIGHT_CATEGORY_DEFAULT')")
public class SysRightCategoryController extends BaseController<ISysRightCategoryApi, SysRightCategoryVO> implements PageController<ISysRightCategoryApi, SysRightCategoryVO> {

    @PostMapping({"/page"})
    @ApiOperation("分页")
    @PreAuthorize("hasAnyRole('SYS_RIGHT_CATEGORY_LIST')")
    public Page<SysRightCategoryVO> page(@RequestBody Page<SysRightCategoryVO> page) {
        return getApi().page(page);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_CATEGORY_INSERT')")
    public IdDTO add(@RequestBody SysRightCategoryVO vo) {
        return PageController.super.add(vo);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_CATEGORY_LIST')")
    public SysRightCategoryVO get(@RequestBody IdDTO vo) {
        return PageController.super.get(vo);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_CATEGORY_UPDATE')")
    public IdDTO update(@RequestBody SysRightCategoryVO vo) {
        return PageController.super.update(vo);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_CATEGORY_DELETE')")
    public IdDTO delete(@RequestBody IdDTO vo) {
        return PageController.super.delete(vo);
    }

    @PreAuthorize("hasAnyRole('SYS_RIGHT_CATEGORY_DELETE')")
    public IdsDTO deleteAll(@RequestBody IdsDTO ids) {
        return PageController.super.deleteAll(ids);
    }
}
