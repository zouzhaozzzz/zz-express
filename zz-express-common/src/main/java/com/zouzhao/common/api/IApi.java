package com.zouzhao.common.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.dto.IViewObject;
import com.zouzhao.common.dto.IdVO;
import com.zouzhao.common.dto.IdsDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
public interface IApi<V extends IViewObject> {
    @PostMapping({"preload"})
    default V preload(@RequestBody V vo) {
        return vo;
    }

    @PostMapping({"add"})
    IdVO add(@RequestBody V var1);

    @PostMapping({"update"})
    void update(@RequestBody V var1);

    @PostMapping({"delete"})
    void delete(@RequestBody IdVO var1);

    @PostMapping({"deleteAll"})
    void deleteAll(@RequestBody IdsDTO var1);

    @PostMapping({"loadById"})
    Optional<V> loadById(@RequestBody IdVO var1);

    @PostMapping({"findAll"})
    Page<V> findAll(@RequestBody Page<V> var1);

    @PostMapping({"getTotal"})
    Long getTotal();

}
