package com.zouzhao.common.core.controller;

import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.IdsDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface DeleteAllController <A extends IApi<V>, V > extends IController<A, V> {
    @PostMapping({"/deleteAll"})
    @ApiOperation("批量删除接口")
    default IdsDTO deleteAll(@RequestBody IdsDTO ids) {
        return this.getApi().deleteAll(ids);
    }
}
