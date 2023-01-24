package com.zouzhao.common.controller;

import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.BaseVO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.common.dto.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface DeleteAllController <A extends IApi<V>, V extends BaseVO> extends IController<A, V> {
    @PostMapping({"deleteAll"})
    @ApiOperation("批量删除接口")
    default Response<?> deleteAll(@RequestBody IdsDTO ids) {
        this.getApi().deleteAll(ids);
        return Response.ok("更新"+ids.getIds()+"成功");
    }
}
