package com.zouzhao.sys.org.core.service;

import cn.hutool.core.util.ObjectUtil;
import com.zouzhao.common.core.service.BaseServiceImpl;
import com.zouzhao.common.core.utils.SelectionUtils;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.sys.org.api.ISysMenuElementApi;
import com.zouzhao.sys.org.core.entity.SysMenuElement;
import com.zouzhao.sys.org.core.entity.SysRightRole;
import com.zouzhao.sys.org.core.mapper.SysMenuElementMapper;
import com.zouzhao.sys.org.dto.SysMenuElementVO;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 姚超
 * @DATE: 2023-1-24
 */
@Service
@RestController
@RequestMapping("/api/sys-org/sysMenuElement")
public class SysMenuElementService extends BaseServiceImpl<SysMenuElementMapper, SysMenuElement, SysMenuElementVO> implements ISysMenuElementApi {

    @Override
    public SysMenuElement voToEntity(SysMenuElementVO vo) {
        SysMenuElement sysMenuElement = new SysMenuElement();
        BeanUtils.copyProperties(vo, sysMenuElement);
        List<SysRightRoleVO> voSysRightRoles = vo.getSysRightRoles();
        List<SysRightRole> roles = new ArrayList<>();
        if (voSysRightRoles != null && voSysRightRoles.size() > 0) {
            voSysRightRoles.forEach(sysRightRoleVO -> {
                SysRightRole sysRightRole = new SysRightRole();
                BeanUtils.copyProperties(sysRightRoleVO, sysRightRole);
                roles.add(sysRightRole);
            });
        }
        if (roles.size() > 0) sysMenuElement.setSysRightRoles(roles);
        return sysMenuElement;
    }

    @Override
    protected void beforeSaveOrUpdate(SysMenuElement entity, boolean isAdd) {
        super.beforeSaveOrUpdate(entity, isAdd);
        //更新菜单权限中间表
        updateRoleRela(entity, isAdd);
        //设置排序号
        if (ObjectUtil.isEmpty(entity.getMenuElementOrder())) entity.setMenuElementOrder(999999999);
    }

    @Override
    protected void afterSaveOrUpdate(SysMenuElement entity, boolean isAdd) {
        super.afterSaveOrUpdate(entity, isAdd);
        List<SysRightRole> sysRightRoles = entity.getSysRightRoles();
        if (isAdd) {
            if (sysRightRoles != null && sysRightRoles.size() > 0) {
                List<String> ids = sysRightRoles.stream().map(SysRightRole::getRightRoleId).collect(Collectors.toList());
                getMapper().insertRoleRela(entity.getMenuElementId(), ids);
            }
        }
    }

    @Override
    protected void beforeDelete(IdDTO vo) {
        super.beforeDelete(vo);
        getMapper().deleteAllRoleRela(vo.getId());
    }

    private void updateRoleRela(SysMenuElement entity, boolean isAdd) {
        if (isAdd) {
            return;
        }
        List<SysRightRole> sysRightRoles = entity.getSysRightRoles();
        List<String> newRoleIds = sysRightRoles.stream().map(SysRightRole::getRightRoleId).collect(Collectors.toList());
        List<String> oldRoleIds = getMapper().findRoleRela(entity.getMenuElementId());
        SelectionUtils.handleSelect(oldRoleIds,newRoleIds,entity.getMenuElementId(),
                (id,ids)->getMapper().insertRoleRela(id, ids),
                (id,ids)->getMapper().deleteRoleRela(id, ids)
                );
    }

    @Override
    public List<SysMenuElementVO> listInRoles(SysMenuElementVO vo) {
        List<SysRightRoleVO> authorities = (List<SysRightRoleVO>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<SysMenuElementVO> voListInRoles = getMapper().findVOListInRoles(authorities, vo);
        //菜单分级
        return loadMenu(voListInRoles);
    }

    public List<SysMenuElementVO> treeData(SysMenuElementVO vo) {
        List<SysMenuElementVO> voList = getMapper().findVOList(voToEntity(vo));
        //菜单分级
        return loadMenu(voList);
    }

    private List<SysMenuElementVO> loadMenu(List<SysMenuElementVO> menus) {
        //遍历出目录
        List<SysMenuElementVO> root = menus.stream()
                .filter(m -> "M".equals(m.getMenuElementType()))
                // .sorted(Comparator.comparing(SysMenuElementVO::getMenuElementOrder))
                .collect(Collectors.toList());
        //遍历加上菜单
        root.forEach(r -> {
            List<SysMenuElementVO> children = menus.stream()
                    .filter(m -> Objects.equals(m.getMenuElementParentId(), r.getMenuElementId()) && "C".equals(m.getMenuElementType()))
                    // .sorted(Comparator.comparing(SysMenuElementVO::getMenuElementOrder))
                    .collect(Collectors.toList());
            r.setMenuChildren(children);
        });
        return root;
    }


}
