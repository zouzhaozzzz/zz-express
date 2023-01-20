package com.zouzhao.common.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.dto.BaseVO;
import com.zouzhao.common.dto.IdsDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
public interface IApi<V extends BaseVO> {

    @PostMapping({"add"})
    BaseVO add(@RequestBody V var1);

    @PostMapping({"update"})
    void update(@RequestBody V var1);

    @PostMapping({"delete"})
    void delete(@RequestBody BaseVO var1);

    @PostMapping({"deleteAll"})
    void deleteAll(@RequestBody IdsDTO var1);

    @PostMapping({"loadById"})
    V loadById(@RequestBody BaseVO var1);

    @PostMapping({"findAll"})
    Page<V> findAll(@RequestBody Page<V> var1);


}
