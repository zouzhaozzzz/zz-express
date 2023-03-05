package com.zouzhao.common.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.api.IPageApi;
import com.zouzhao.common.core.entity.BaseEntity;
import com.zouzhao.common.core.mapper.IPageMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 姚超
 * @DATE: 2023-2-4
 */
@Transactional(
        readOnly = true,
        rollbackFor = {}
)
public abstract class PageServiceImpl<M extends IPageMapper<E,V>, E extends BaseEntity, V> extends BaseServiceImpl<M,E,V> implements IPageApi<V> {

    public Page<V> page(@RequestBody Page<V> page){
        Page<V> page1 = getMapper().page(page, ObjectUtils.isEmpty(page.getRecords()) ? null : page.getRecords().get(0));
        return page1;
    }
}
