package com.zouzhao.common.controller;

import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.IdDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface CrudController<A extends IApi<V>, V > extends IController<A,V>{
    @PostMapping({"/add"})
    @ApiOperation("新增接口")
    default IdDTO add(@RequestBody V vo) {
        String id = this.getApi().add(vo).getId();
        if(("-1".equals(id))) return IdDTO.of("-1");
        return IdDTO.of(id);
    }

    @PostMapping({"/get"})
    @ApiOperation("查看接口")
    default V get(@RequestBody IdDTO vo) {
        V result = this.getApi().findVOById(vo);
        if (ObjectUtils.isEmpty(result)) {
            return null;
        } else {
            return result;
        }
    }

    @PostMapping({"/update"})
    @ApiOperation("更新接口")
    default IdDTO update(@RequestBody V vo) {
        return this.getApi().update(vo);
    }

    @PostMapping({"/delete"})
    @ApiOperation("删除接口")
    default IdDTO delete(@RequestBody IdDTO vo) {
        return this.getApi().delete(vo);
    }
}
