package com.zouzhao.common.api;

import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
public interface IApi<V> {

    @PostMapping({"/add"})
    IdDTO add(@RequestBody V var1);

    @PostMapping({"/update"})
    IdDTO update(@RequestBody V var1);

    @PostMapping({"/delete"})
    IdDTO delete(@RequestBody IdDTO var1);

    @PostMapping({"/deleteAll"})
    IdsDTO deleteAll(@RequestBody IdsDTO var1);

    @PostMapping({"/loadById"})
    V findVOById(@RequestBody IdDTO var1);

    @PostMapping({"/findAll"})
    List<V> findAll(@RequestBody V var1);


}
