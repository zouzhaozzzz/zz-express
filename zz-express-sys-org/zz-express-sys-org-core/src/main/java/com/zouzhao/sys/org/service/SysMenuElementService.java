package com.zouzhao.sys.org.service;

import com.zouzhao.common.service.BaseServiceImpl;
import com.zouzhao.sys.org.api.ISysMenuElementApi;
import com.zouzhao.sys.org.dto.SysMenuElementVO;
import com.zouzhao.sys.org.entity.SysMenuElement;
import com.zouzhao.sys.org.entity.SysRightRole;
import com.zouzhao.sys.org.mapper.SysMenuElementMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
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
        return sysMenuElement;
    }

    @Override
    public List<SysMenuElementVO> listInRoles(SysMenuElementVO vo) {
        List<SysRightRole> authorities = (List<SysRightRole>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<SysMenuElementVO> voListInRoles = getMapper().findVOListInRoles(authorities, vo);
        //菜单分级
        return loadMenu(voListInRoles);
    }

    @Override
    public List<SysMenuElementVO> findAll(SysMenuElementVO vo) {
        List<SysMenuElementVO> voListInRoles = getMapper().findVOList(voToEntity(vo));
        //菜单分级
        return loadMenu(voListInRoles);
    }

    private List<SysMenuElementVO> loadMenu(List<SysMenuElementVO> menus) {
        //遍历出目录
        List<SysMenuElementVO> root = menus.stream()
                .filter(m -> "M".equals(m.getMenuElementType()))
                .sorted(Comparator.comparing(SysMenuElementVO::getMenuElementOrder))
                .collect(Collectors.toList());
        //遍历加上菜单
        root.forEach(r -> {
            List<SysMenuElementVO> children = menus.stream()
                    .filter(m -> Objects.equals(m.getMenuElementParentId(), r.getMenuElementId()) && "C".equals(m.getMenuElementType()))
                    .sorted(Comparator.comparing(SysMenuElementVO::getMenuElementOrder))
                    .collect(Collectors.toList());
            r.setMenuChildren(children);
        });
        return root;
    }
}
