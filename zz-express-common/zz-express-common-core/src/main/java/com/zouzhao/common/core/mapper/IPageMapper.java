package com.zouzhao.common.core.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author 姚超
 * @DATE: 2023-2-4
 */
// @NoRepositoryBean
public interface IPageMapper<E,V> extends IMapper<E,V>{

    Page<V> page(Page<V> page, @Param("v") V e);
}
