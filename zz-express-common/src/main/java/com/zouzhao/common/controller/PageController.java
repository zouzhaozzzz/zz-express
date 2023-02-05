package com.zouzhao.common.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.api.IPageApi;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 姚超
 * @DATE: 2023-2-4
 */
public interface PageController<A extends IPageApi<V>, V> extends CombineController<A, V> {

    @PostMapping({"/page"})
    @ApiOperation("分页")
    default Page<V> page(@RequestBody Page<V> page) {
        return getApi().page(page);
    }
}
