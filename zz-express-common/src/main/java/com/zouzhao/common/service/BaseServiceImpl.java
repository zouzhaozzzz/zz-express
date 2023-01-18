package com.zouzhao.common.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zouzhao.common.dao.IRepository;
import com.zouzhao.common.entity.BaseEntity;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
public  class BaseServiceImpl<R extends IRepository<E>,E extends BaseEntity> extends ServiceImpl<R,E> {
}
