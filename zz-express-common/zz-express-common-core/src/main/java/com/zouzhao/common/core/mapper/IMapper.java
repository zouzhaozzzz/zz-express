package com.zouzhao.common.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
// @NoRepositoryBean
public interface IMapper<E,V> extends BaseMapper<E> {
    List<E> findList(E e);

    List<V>findVOList(E e);

    E findById(String id);

    V findVOById(String id);

}
