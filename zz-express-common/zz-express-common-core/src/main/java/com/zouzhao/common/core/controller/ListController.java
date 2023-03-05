package com.zouzhao.common.core.controller;

import com.zouzhao.common.api.IApi;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface ListController<A extends IApi<V>, V> extends IController<A, V> {

    @PostMapping({"/list"})
    @ApiOperation("列表查询接口")
    default List<V> list(@RequestBody V request) {
        return this.getApi().findAll(request);
    }


}

