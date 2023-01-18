package com.zouzhao.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@NoRepositoryBean
public interface IRepository<E> extends BaseMapper<E> {
}
