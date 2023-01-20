package com.zouzhao.common.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dao.IRepository;
import com.zouzhao.common.dto.BaseVO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.common.entity.BaseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
public class BaseServiceImpl<R extends IRepository<E>, E extends BaseEntity, V extends BaseVO> extends ServiceImpl<R, E> implements IApi<V> {


    public BaseVO add(V vo) {
           E e = voToEntity(vo);
        super.save(e);
        return BaseVO.of(e.getFdId());
    }


    public void update(V vo) {
        E e = voToEntity(vo);
        if (ObjectUtils.isEmpty(super.getById(e.getFdId()))) {
            super.save(e);
        } else super.updateById(e);
    }


    public void delete(BaseVO vo) {
        super.removeById(vo.getFdId());
    }


    public void deleteAll(IdsDTO idsDTO) {
        super.removeBatchByIds(idsDTO.getFdIds());
    }


    public V loadById(BaseVO var1) {
        E e = super.getById(var1.getFdId());
        return entityToVo(e);
    }


    public Page<V> findAll(Page<V> var1) {
        Page<E> result = super.page(Page.of(var1.getCurrent(), var1.getSize()));
        Page<V> page = new Page<>();
        BeanUtils.copyProperties(result,page);
        List<V> records = page.getRecords();
        result.getRecords().forEach(e -> {
            V instance = (V)new BaseVO();
            BeanUtils.copyProperties(e, instance);
            records.add(instance);
        });
        return page;
    }

    protected E voToEntity(V vo) {
        E e = (E) new BaseEntity();
        BeanUtils.copyProperties(vo, e);
        return e;
    }

    protected V entityToVo(E entity) {
        V v = (V) new BaseVO();
        BeanUtils.copyProperties(entity, v);
        return v;
    }

}
