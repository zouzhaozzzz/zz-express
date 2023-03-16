package com.zouzhao.sys.org.core.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.common.core.service.PageServiceImpl;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.sys.org.api.ISysOrgAccountApi;
import com.zouzhao.sys.org.api.ISysOrgElementApi;
import com.zouzhao.sys.org.core.entity.SysOrgElement;
import com.zouzhao.sys.org.core.mapper.SysOrgElementMapper;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private ISysOrgAccountApi sysOrgAccountService;

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
        if (entity.getOrgElementOrder() == null) entity.setOrgElementOrder(999999999);
        //如果为更新
        if (!isAdd) {
            //判断上级，防止循环
            String parentId = entity.getOrgElementParentId();
            String id = entity.getId();
            judgeParent(parentId, id);
        }
    }

    private void judgeParent(String parentId, String id) {
        if (StrUtil.isNotBlank(parentId) && !parentId.equals("0")) {
            if (parentId.equals(id)) throw new MyException("存在循环嵌套关系");
            judgeParent(getMapper().findById(parentId).getOrgElementParentId(), id);
        }
    }

    @Override
    protected void beforeDeleteAll(IdsDTO idsDTO) {
        super.beforeDeleteAll(idsDTO);
    }

    @Override
    @Transactional
    public IdDTO add(SysOrgElementVO vo) {
        IdDTO result = super.add(vo);
        //如果为人员，需要对账号account操作
        if (vo.getOrgElementType() == 1) {
            String loginName = vo.getOrgElementLoginName();
            if (StrUtil.isBlank(loginName)) throw new MyException("登录名为空");
            SysOrgAccountVO exist = sysOrgAccountService.findVOByLoginName(loginName);
            if (exist != null) throw new RuntimeException("用户名已存在");
            SysOrgAccountVO accountVO = new SysOrgAccountVO();
            accountVO.setOrgAccountLoginName(loginName);
            accountVO.setOrgAccountPassword(vo.getOrgElementPassword());
            accountVO.setOrgAccountDefPersonId(result.getId());
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
                .filter(m -> StrUtil.isBlank(m.getOrgElementParentId()) || "0".equals(m.getOrgElementParentId()))
                // .sorted(Comparator.comparing(SysOrgElementVO::getOrgElementName))
                .collect(Collectors.toList());
        //菜单分级
        loadMenu(root, voList);
        return root;
    }

    @Override
    @Transactional
    public void disableAll(IdsDTO idsDTO) {
        List<String> ids = idsDTO.getIds();
        if (ids == null || ids.size() < 1) return;
        //批量更新状态
        getMapper().batchUpdateStatus(ids, false);
    }

    @Override
    public int countOrg() {
        return getMapper().countOrg();
    }

    @Override
    public int countPerson() {
        return getMapper().countPerson();
    }

    @Override
    public List<SysOrgElementVO> listInRoles(SysOrgElementVO request) {
        Integer type = request.getOrgElementType();
        if (ObjectUtil.isEmpty(type)) throw new MyException("组织类型为空");
        List<SysOrgElementVO> data = new ArrayList<>();
        //组织
        //拿到当前登陆人的组织
        SysOrgElementVO org = getMapper().findByLoginName(request);
        //当前人员没有分配组织，查不到组织
        if (StrUtil.isEmpty(org.getOrgElementOrgId()) && type == 0) return null;
        //当前人员没有分配组织，人员只能查自己
        if (StrUtil.isEmpty(org.getOrgElementOrgId()) && type == 1) {
            data.add(org);
            return data;
        }

        SysOrgElementVO parent = getMapper().findVOById(org.getOrgElementOrgId());
        if (parent == null) return null;
        data.add(parent);
        //添加下级组织
        addChildrenOrg(parent, data);

        if (type == 0) {
            return data;
        } else {
            //人员
            //当前人员有组织，查当前组织和下级组织的人员
            return getMapper().findVOListInOrgs(request, data);
        }
    }

    private void addChildrenOrg(SysOrgElementVO org, List<SysOrgElementVO> data) {
        List<SysOrgElementVO> child = getMapper().findChildOrgById(org);
        if (child != null && child.size() > 0) {
            data.addAll(child);
            child.forEach(e -> addChildrenOrg(e, data));
        }
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
