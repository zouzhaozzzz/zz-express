package com.zouzhao.sys.org.service;

import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.common.service.PageServiceImpl;
import com.zouzhao.sys.org.api.ISysRightGroupApi;
import com.zouzhao.sys.org.dto.SysRightGroupVO;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import com.zouzhao.sys.org.entity.SysRightGroup;
import com.zouzhao.sys.org.mapper.SysRightGroupMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
@Service
@RestController
@RequestMapping("/api/sys-org/sysRightGroup")
public class SysRightGroupService extends PageServiceImpl<SysRightGroupMapper, SysRightGroup, SysRightGroupVO> implements ISysRightGroupApi {


    @Override
    public SysRightGroup voToEntity(SysRightGroupVO vo) {
        SysRightGroup sysRightGroup = new SysRightGroup();
        BeanUtils.copyProperties(vo, sysRightGroup);
        return sysRightGroup;
    }

    @Override
    @Transactional
    public IdDTO add(SysRightGroupVO vo) {
        List<SysRightRoleVO> roles = vo.getSysRightRoles();
        IdDTO id = super.add(vo);
        if (roles != null && roles.size() > 0)
            getMapper().addRoleRela( roles,id.getId());
        return id;
    }

    @Override
    @Transactional
    public IdDTO update(SysRightGroupVO vo) {
        List<SysRightRoleVO> roles = vo.getSysRightRoles();
        IdDTO idDTO = super.update(vo);

        getMapper().removeRela(idDTO.getId());

        if (roles != null && roles.size() > 0)
            getMapper().addRoleRela(roles,idDTO.getId());
        return idDTO;
    }

    @Override
    public IdDTO delete(IdDTO vo) {
        getMapper().removeRela(vo.getId());
        return super.delete(vo);
    }

    @Override
    public IdsDTO deleteAll(IdsDTO idsDTO) {
        getMapper().removeRelaList(idsDTO.getIds());
        return super.deleteAll(idsDTO);
    }
}
