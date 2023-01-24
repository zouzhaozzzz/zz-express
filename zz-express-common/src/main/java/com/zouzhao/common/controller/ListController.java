package com.zouzhao.common.controller;

import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.BaseVO;
import com.zouzhao.common.dto.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface ListController<A extends IApi<V>, V extends BaseVO> extends IController<A, V> {

    @PostMapping({"list"})
    @ApiOperation("列表查询接口")
    default Response<List<V>> list(@RequestBody V request) {
        return Response.ok(this.getApi().findAll(request));
    }


}

