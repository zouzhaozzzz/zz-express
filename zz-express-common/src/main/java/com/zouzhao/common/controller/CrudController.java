package com.zouzhao.common.controller;

import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.BaseVO;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface CrudController<A extends IApi<V>, V extends BaseVO> extends IController<A,V>{
    @PostMapping({"add"})
    @ApiOperation("新增接口")
    default Response<IdDTO> add(@RequestBody V vo) {
        return Response.ok("成功",this.getApi().add(vo));
    }

    @PostMapping({"get"})
    @ApiOperation("查看接口")
    default Response<V> get(@RequestBody IdDTO vo) {
        V result = this.getApi().findVOById(vo);
        if (ObjectUtils.isEmpty(result)) {
            throw new RuntimeException("不存在id");
        } else {
            return Response.ok("成功",result);
        }
    }

    @PostMapping({"update"})
    @ApiOperation("更新接口")
    default Response<?> update(@RequestBody V vo) {
        this.getApi().update(vo);
        return Response.ok("更新"+vo.getId()+"成功");
    }

    @PostMapping({"delete"})
    @ApiOperation("删除接口")
    default Response<?> delete(@RequestBody IdDTO vo) {
        this.getApi().delete(vo);
        return Response.ok("删除"+vo.getId()+"成功");
    }
}
