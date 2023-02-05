package com.zouzhao.common.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
public interface IPageApi<V> extends IApi<V>{

    @PostMapping({"/page"})
   Page<V> page(@RequestBody Page<V> page);

}
