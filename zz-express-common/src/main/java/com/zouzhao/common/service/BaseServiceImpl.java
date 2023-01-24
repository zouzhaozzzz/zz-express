package com.zouzhao.common.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.BaseVO;
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
public abstract class BaseServiceImpl<M extends IMapper<E,V>, E extends BaseEntity, V extends BaseVO> extends ServiceImpl<M, E> implements IApi<V> {


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
        super.save(e);
        return IdDTO.of(e.getId());
    }


    @Transactional(
            rollbackFor = {}
    )
    public void update(V vo) {
        E e = voToEntity(vo);
        if (ObjectUtils.isEmpty(super.getById(e.getId()))) {
            super.save(e);
        } else super.updateById(e);
    }


    @Transactional(
            rollbackFor = {}
    )
    public void delete(IdDTO vo) {
        super.removeById(vo.getId());
    }


    @Transactional(
            rollbackFor = {}
    )
    public void deleteAll(IdsDTO idsDTO) {
        super.removeBatchByIds(idsDTO.getIds());
    }


    public V findVOById(IdDTO vo) {
        return getMapper().findVOById(vo.getId());
    }

    @Override
    public List<V> findAll(V var1) {
        E e = voToEntity(var1);
        return getMapper().findVOList(e);
    }

    public abstract E voToEntity(V vo);



}
