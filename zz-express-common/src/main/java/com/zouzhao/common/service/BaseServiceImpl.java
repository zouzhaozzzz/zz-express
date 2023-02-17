package com.zouzhao.common.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.common.entity.BaseEntity;
import com.zouzhao.common.mapper.IMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@Transactional(
        readOnly = true,
        rollbackFor = {}
)
public abstract class BaseServiceImpl<M extends IMapper<E,V>, E extends BaseEntity, V > extends ServiceImpl<M, E> implements IApi<V> {


    @Autowired
    M mapper;

    public M getMapper() {
        return this.mapper;
    }

    @Transactional(
            rollbackFor = {}
    )
    public IdDTO add(V vo) {
        E e = voToEntity(vo);
        beforeSaveOrUpdate(e,true);
        super.save(e);
        afterSaveOrUpdate(e,true);
        return IdDTO.of(e.getId());
    }


    @Transactional(
            rollbackFor = {}
    )
    public IdDTO update(V vo) {
        E e = voToEntity(vo);
        beforeSaveOrUpdate(e,false);
        if (ObjectUtils.isEmpty(super.getById(e.getId()))) {
            super.save(e);
            afterSaveOrUpdate(e,true);
        } else {
            super.updateById(e);
            afterSaveOrUpdate(e,false);
        }

        return IdDTO.of(e.getId());
    }


    @Transactional(
            rollbackFor = {}
    )
    public IdDTO delete(IdDTO vo) {
        beforeDelete(vo);
        super.removeById(vo.getId());
        return vo;
    }


    @Transactional(
            rollbackFor = {}
    )
    public IdsDTO deleteAll(IdsDTO idsDTO) {
        beforeDeleteAll(idsDTO);
        super.removeBatchByIds(idsDTO.getIds());
        return  idsDTO;
    }

    protected  void beforeDeleteAll(IdsDTO idsDTO) {

    }


    public V findVOById(IdDTO vo) {
        return getMapper().findVOById(vo.getId());
    }

    @Override
    public List<V> findAll(V var1) {
        E e = voToEntity(var1);
        return getMapper().findVOList(e);
    }

    protected abstract E voToEntity(V vo);


    protected void beforeSaveOrUpdate(E entity, boolean isAdd) {
    }

    protected void afterSaveOrUpdate(E entity, boolean isAdd) {
    }

    protected void beforeDelete(IdDTO vo ) {
    }


}
