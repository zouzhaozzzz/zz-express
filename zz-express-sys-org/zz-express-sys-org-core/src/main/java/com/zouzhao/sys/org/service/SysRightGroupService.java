package com.zouzhao.sys.org.service;

import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.common.exception.MyException;
import com.zouzhao.common.service.PageServiceImpl;
import com.zouzhao.common.utils.SelectionUtils;
import com.zouzhao.sys.org.api.ISysOrgAccountApi;
import com.zouzhao.sys.org.api.ISysRightGroupApi;
import com.zouzhao.sys.org.dto.SysRightGroupVO;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import com.zouzhao.sys.org.entity.SysRightGroup;
import com.zouzhao.sys.org.mapper.SysRightGroupMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
@Service
@RestController
@RequestMapping("/api/sys-right/sysRightGroup")
public class SysRightGroupService extends PageServiceImpl<SysRightGroupMapper, SysRightGroup, SysRightGroupVO> implements ISysRightGroupApi {


    @Autowired
    private ISysOrgAccountApi sysOrgAccountApi;


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
        if (roles != null && roles.size() > 0) {
            List<String> list = roles.stream().map(SysRightRoleVO::getRightRoleId).collect(Collectors.toList());
            getMapper().addRoleRela(list, id.getId());
        }

        return id;
    }

    @Override
    @Transactional
    public IdDTO update(SysRightGroupVO vo) {
        List<SysRightRoleVO> roles = vo.getSysRightRoles();
        IdDTO idDTO = super.update(vo);

        getMapper().removeRela(idDTO.getId());

        if (roles != null && roles.size() > 0) {
            List<String> list = roles.stream().map(SysRightRoleVO::getRightRoleId).collect(Collectors.toList());
            getMapper().addRoleRela(list, idDTO.getId());
        }
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

    @Override
    public List<String> listPersonIdByAccount(IdDTO idDTO) {
        return getMapper().listPersonIdByAccount(idDTO.getId());
    }

    @Override
    @Transactional
    public void allotToAccount(SysRightGroupVO vo) {
        String rightGroupId = vo.getRightGroupId();
        List<String> elementIds = vo.getElementIds();
        //通过elementId拿到accountId
        List<String> newIds=elementIdsToAccountIds(elementIds);
        if (rightGroupId == null) throw new MyException("角色id为空");
        List<String> oldIds = getMapper().listAccountIdsByGroupId(rightGroupId);
        SelectionUtils.handleSelect(oldIds, newIds, rightGroupId,
                (id, ids) -> {
                    getMapper().insertAccountRela(id, ids);
                },
                (id,ids)-> {
                    getMapper().deleteAccountRela(id, ids);
                }
                );
    }

    private List<String> elementIdsToAccountIds(List<String> elementIds){
        return sysOrgAccountApi.getIdsByDefPersonIds(elementIds);
    }
}
