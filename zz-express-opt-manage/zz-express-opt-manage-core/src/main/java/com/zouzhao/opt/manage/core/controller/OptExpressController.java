package com.zouzhao.opt.manage.core.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.controller.PageController;
import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import com.zouzhao.sys.org.client.SysOrgElementClient;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@RestController
@RequestMapping("/data/opt-manage/optExpress")
@Api(
        tags = "运单管理"
)
@PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_DEFAULT','OPT_MANAGE_EXPRESS_ADMIN')")
public class OptExpressController extends BaseController<IOptExpressApi, OptExpressVO> implements PageController<IOptExpressApi, OptExpressVO> {

    @Autowired
    private SysOrgElementClient sysOrgElementClient;

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_INSERT','OPT_MANAGE_EXPRESS_ADMIN')")
    public IdDTO add(@RequestBody OptExpressVO vo) {
        return PageController.super.add(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_LIST','OPT_MANAGE_EXPRESS_ADMIN')")
    public OptExpressVO get(@RequestBody IdDTO vo) {
        return PageController.super.get(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_UPDATE','OPT_MANAGE_EXPRESS_ADMIN')")
    public IdDTO update(@RequestBody OptExpressVO vo) {
        return PageController.super.update(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_UPDATE','OPT_MANAGE_EXPRESS_ADMIN')")
    @PostMapping("/updateStatusBatch")
    public ResponseEntity<String> updateStatusBatch(@RequestBody OptExpressVO vo) {
        List<String> idList = vo.getExpressIdList();
        Integer status = vo.getExpressStatus();
        if (ObjectUtil.isEmpty(idList) || status == null) throw new MyException("未传入ids或状态");
        getApi().updateStatusBatch(idList, status);
        return new ResponseEntity("修改成功", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_DELETE','OPT_MANAGE_EXPRESS_ADMIN')")
    public IdDTO delete(@RequestBody IdDTO vo) {
        return PageController.super.delete(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_DELETE','OPT_MANAGE_EXPRESS_ADMIN')")
    public IdsDTO deleteAll(@RequestBody IdsDTO ids) {
        return PageController.super.deleteAll(ids);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_LIST','OPT_MANAGE_EXPRESS_ADMIN')")
    public List<OptExpressVO> list(@RequestBody OptExpressVO request) {
        return PageController.super.list(request);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_LIST','OPT_MANAGE_EXPRESS_ADMIN')")
    public Page<OptExpressVO> page(@RequestBody Page<OptExpressVO> page) {
        if (ObjectUtils.isEmpty(page.getRecords()) || ObjectUtils.isEmpty(page.getRecords().get(0)) || ObjectUtil.isEmpty(page.getRecords().get(0).getExpressStatusFlag())) {
            throw new MyException("相关信息传入不全");
        }
        OptExpressVO optExpressVO =page.getRecords().get(0);

        //查询过滤
        //是否差寄派件公司  1查寄件公司 2查派件公司
        Integer searchCompany = optExpressVO.getExpressStatusFlag();
        if(searchCompany == 1 && StrUtil.isEmpty(optExpressVO.getSendCustomerId()) && StrUtil.isEmpty(optExpressVO.getSendCompanyId()))
        {
            //拿到当前登录人的组织
            SysOrgElementVO elementVO = getCurUser();
            if(elementVO == null || StrUtil.isEmpty(elementVO.getOrgElementOrgId())){
               return page;
            }
           optExpressVO.setSendCustomerId(elementVO.getOrgElementId());
           optExpressVO.setSendCompanyId(elementVO.getOrgElementOrgId());
        }else if(searchCompany == 2 && StrUtil.isEmpty(optExpressVO.getConsigneeCustomerId()) && StrUtil.isEmpty(optExpressVO.getConsigneeCustomerId())){
            //拿到当前登录人的组织
            SysOrgElementVO elementVO = getCurUser();
            if(elementVO == null || StrUtil.isEmpty(elementVO.getOrgElementOrgId())){
                return page;
            }
            optExpressVO.setConsigneeCustomerId(elementVO.getOrgElementId());
            optExpressVO.setConsigneeCompanyId(elementVO.getOrgElementOrgId());
        }

        //分页查询
        Page<OptExpressVO> pageResult = getApi().pagePlus(page);
        List<OptExpressVO> list = pageResult.getRecords();
        //填充
        if (list != null && list.size() > 0) list.forEach(
                expressVO -> {
                    List<String> ids = new ArrayList<>();
                    //寄件客户
                    String sendCustomerId = expressVO.getSendCustomerId();
                    if (ObjectUtil.isNotEmpty(sendCustomerId)) {
                        SysOrgElementVO result = sysOrgElementClient.findVOById(IdDTO.of(sendCustomerId));
                        if (ObjectUtil.isNotEmpty(result)) expressVO.setSendCustomer(result);
                    }
                    //寄件客户所属公司
                    String sendCompanyId = expressVO.getSendCompanyId();
                    if (ObjectUtil.isNotEmpty(sendCompanyId)) {
                        SysOrgElementVO result = sysOrgElementClient.findVOById(IdDTO.of(sendCompanyId));
                        if (ObjectUtil.isNotEmpty(result)) expressVO.setSendCompany(result);
                    }
                    //收件客户
                    String consigneeCustomerId = expressVO.getConsigneeCustomerId();
                    if (ObjectUtil.isNotEmpty(consigneeCustomerId)) {
                        SysOrgElementVO result = sysOrgElementClient.findVOById(IdDTO.of(consigneeCustomerId));
                        if (ObjectUtil.isNotEmpty(result)) expressVO.setConsigneeCustomer(result);
                    }
                    //收件客户所属公司
                    String consigneeCompanyId = expressVO.getConsigneeCompanyId();
                    if (ObjectUtil.isNotEmpty(consigneeCompanyId)) {
                        SysOrgElementVO result = sysOrgElementClient.findVOById(IdDTO.of(consigneeCompanyId));
                        if (ObjectUtil.isNotEmpty(result)) expressVO.setConsigneeCompany(result);
                    }
                }
        );
        return pageResult;
    }

    //拿到当前登录人
    private SysOrgElementVO getCurUser() {
        String loginName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysOrgElementVO request = new SysOrgElementVO();
        request.setOrgElementLoginName(loginName);
        SysOrgElementVO elementVO = sysOrgElementClient.findVOByLoginName(request);
        return elementVO;
    }


}
