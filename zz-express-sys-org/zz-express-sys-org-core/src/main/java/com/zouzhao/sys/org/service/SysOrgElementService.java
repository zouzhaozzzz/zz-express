package com.zouzhao.sys.org.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.exception.MyException;
import com.zouzhao.common.service.PageServiceImpl;
import com.zouzhao.sys.org.api.ISysOrgElementApi;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import com.zouzhao.sys.org.entity.SysOrgElement;
import com.zouzhao.sys.org.mapper.SysOrgElementMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 姚超
 * @DATE: 2023-2-17
 */
@Service
@RestController
@RequestMapping("/api/sys-org/sysOrgElement")
public class SysOrgElementService extends PageServiceImpl<SysOrgElementMapper, SysOrgElement, SysOrgElementVO> implements ISysOrgElementApi {

    @Autowired
    private SysOrgAccountService sysOrgAccountService;

    @Override
    protected SysOrgElement voToEntity(SysOrgElementVO vo) {
        SysOrgElement element = new SysOrgElement();
        BeanUtils.copyProperties(vo, element);
        return element;
    }

    @Override
    protected void beforeSaveOrUpdate(SysOrgElement entity, boolean isAdd) {
        super.beforeSaveOrUpdate(entity, isAdd);
        //设置排序号
        if(StringUtils.isEmpty(entity.getOrgElementOrder()))entity.setOrgElementOrder(999999999);
        //如果为更新
        if (!isAdd) {
            //判断上级，防止循环
            String parentId=entity.getOrgElementParentId();
            String id = entity.getId();
            judgeParent(parentId, id);
        }
    }

    private void judgeParent(String parentId, String id) {
        if (!StringUtils.isEmpty(parentId) && !parentId.equals("0")) {
            if (parentId.equals(id)) throw new MyException("存在循环嵌套关系");
            judgeParent(getMapper().findById(parentId).getOrgElementParentId(),id);
        }
    }



    @Override
    @Transactional
    public IdDTO add(SysOrgElementVO vo) {
        IdDTO result = super.add(vo);
        //如果为人员，需要对账号account操作
        if (vo.getOrgElementType() == 1) {
            SysOrgAccountVO accountVO = new SysOrgAccountVO();
            accountVO.setOrgAccountLoginName(vo.getOrgElementLoginName());
            accountVO.setOrgAccountPassword(vo.getOrgElementPassword());
            sysOrgAccountService.add(accountVO);
        }
        return result;
    }

    @Override
    public Page<SysOrgElementVO> page(Page<SysOrgElementVO> page) {
        List<SysOrgElementVO> orgElementList = super.page(page).getRecords();
        //填充
        if (orgElementList != null && orgElementList.size() > 0) orgElementList.forEach(
                orgElement -> {
                    List<String> ids = new ArrayList<>();
                    //上级
                    if (ObjectUtil.isNotEmpty(orgElement.getOrgElementParentId())) {
                        SysOrgElementVO result = getMapper().findVOById(orgElement.getOrgElementParentId());
                        if (ObjectUtil.isNotEmpty(result)) orgElement.setOrgElementParent(result);
                    }
                    //领导
                    if (ObjectUtil.isNotEmpty(orgElement.getOrgElementThisLeaderId())) {
                        SysOrgElementVO result = getMapper().findVOById(orgElement.getOrgElementThisLeaderId());
                        if (ObjectUtil.isNotEmpty(result)) orgElement.setOrgElementThisLeader(result);
                    }
                    //组织
                    if (ObjectUtil.isNotEmpty(orgElement.getOrgElementOrgId())) {
                        SysOrgElementVO result = getMapper().findVOById(orgElement.getOrgElementOrgId());
                        if (ObjectUtil.isNotEmpty(result)) orgElement.setOrgElementOrg(result);
                    }
                }
        );
        return page;
    }



    @Override
    public List<SysOrgElementVO> treeData(SysOrgElementVO request) {
        List<SysOrgElementVO> voList = findAll(request);
        //顶级菜单
        List<SysOrgElementVO> root = voList.stream()
                .filter(m -> StringUtils.isEmpty(m.getOrgElementParentId()) || "0".equals(m.getOrgElementParentId()))
                // .sorted(Comparator.comparing(SysOrgElementVO::getOrgElementName))
                .collect(Collectors.toList());
        //菜单分级
        loadMenu(root, voList);
        return root;
    }

    private void loadMenu(List<SysOrgElementVO> root, List<SysOrgElementVO> menu) {
        //遍历加上菜单
        root.forEach(parent -> {
            List<SysOrgElementVO> children = menu.stream()
                    .filter(m -> Objects.equals(m.getOrgElementParentId(), parent.getOrgElementId()))
                    // .sorted(Comparator.comparing(SysOrgElementVO::getOrgElementName))
                    .collect(Collectors.toList());
            parent.setChildren(children);
            loadMenu(children, menu);
        });
    }


}
