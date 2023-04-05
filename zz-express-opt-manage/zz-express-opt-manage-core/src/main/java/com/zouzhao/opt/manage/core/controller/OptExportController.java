package com.zouzhao.opt.manage.core.controller;

import cn.hutool.core.util.ObjectUtil;
import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.common.security.utils.RedisManager;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.dto.OptExpressMonthFeeVO;
import com.zouzhao.opt.manage.dto.OptExpressMonthNumVO;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import com.zouzhao.sys.org.client.SysOrgElementClient;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@RestController
@RequestMapping("/data/opt-manage/optExport")
@Api(
        tags = "运营报表"
)
@PreAuthorize("hasAnyRole('OPT_MANAGE_REPORT_DEFAULT','OPT_MANAGE_REPORT_ADMIN')")
public class OptExportController extends BaseController<IOptExpressApi, OptExpressVO> {

    @Autowired
    private SysOrgElementClient sysOrgElementClient;
    @Autowired
    private RedisManager redisManager;

    private final String[] month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",};


    @PostMapping("/countExpressNum")
    @ApiOperation("统计符合条件的物流数量")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_LIST','OPT_MANAGE_EXPRESS_ADMIN')")
    public int countExpressNum(@RequestBody OptExpressVO vo) {
        return getApi().countExpressNum(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_REPORT_LIST','OPT_MANAGE_EXPRESS_ADMIN')")
    @ApiOperation("从redis中拿统计数据")
    @PostMapping("/export")
    public Map<String, Object> export(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        Object key = request.get("key");
        Object relation = request.get("relation");
        if (ObjectUtil.isEmpty(key) || ObjectUtil.isEmpty(relation)) throw new MyException("报表查询的条件不全");
        //拿到当前登录人的组织和下级组织
        List<SysOrgElementVO> orgList = getCurrentUserOrgList();
        if (orgList == null || orgList.size() < 1) return null;
        switch ((String) key) {
            // "统计物流状态"
            case "countStatus":
                countStatus(result, (boolean) relation, orgList);
                break;
            // "省份寄件派送个数"
            case "countByProvinces":
                result.put("province", redisManager.getHashValue("report-express:province", "province"));
                break;
            //"每月的问题件，退货件，每月的总件数统计"
            case "countFlagByMonth":
                countFlagByMonth(result, (boolean) relation, orgList);
                break;
            //"每月的成本费（寄件代收货款手续费、到付手续费成本、中转费成本、面单成本），保费收入，运费，罚款，收入统计"
            case "countFeeByMonth":
                countFeeByMonth(result, (boolean) relation, orgList);
                break;
        }
        return result;
    }

    //拿到当前登录人的组织和下级组织
    private List<SysOrgElementVO> getCurrentUserOrgList() {
        String loginName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysOrgElementVO request = new SysOrgElementVO();
        request.setOrgElementType(0);
        request.setOrgElementLoginName(loginName);
        return sysOrgElementClient.listInRoles(request);
    }

    //"每月的成本费（寄件代收货款手续费、到付手续费成本、中转费成本、面单成本），保费收入，运费，罚款，收入统计"
    private void countFeeByMonth(Map<String, Object> result, boolean relation, List<SysOrgElementVO> orgList) {
        List<OptExpressMonthFeeVO> totalCostList = new ArrayList<>(12);
        List<OptExpressMonthFeeVO> premiumList = new ArrayList<>(12);
        List<OptExpressMonthFeeVO> sendFineList = new ArrayList<>(12);
        List<OptExpressMonthFeeVO> freightList = new ArrayList<>(12);
        List<OptExpressMonthFeeVO> income = new ArrayList<>(12);
        result.put("totalCostByMonth", totalCostList);
        result.put("premiumByMonth", premiumList);
        result.put("sendFineByMonth", sendFineList);
        result.put("freightByMonth", freightList);
        result.put("incomeByMonth", income);
        if (relation) {
            orgList.forEach(org -> {
                String elementId = org.getOrgElementId();
                feeForMonth(totalCostList, premiumList, sendFineList, freightList, income, elementId);
            });
        } else {
            String orgElementId = orgList.get(0).getOrgElementId();
            feeForMonth(totalCostList, premiumList, sendFineList, freightList, income, orgElementId);
        }

    }


    private void feeForMonth(List<OptExpressMonthFeeVO> totalCostList, List<OptExpressMonthFeeVO> premiumList, List<OptExpressMonthFeeVO> sendFineList, List<OptExpressMonthFeeVO> freightList, List<OptExpressMonthFeeVO> income, String orgElementId) {
        for (int i = 0; i < month.length; i++) {
            String monthValue = month[i];
            setFeeMonthValue(totalCostList, orgElementId, i, monthValue, "report-express:totalCostByMonth:");
            setFeeMonthValue(premiumList, orgElementId, i, monthValue, "report-express:premiumByMonth:");
            setFeeMonthValue(sendFineList, orgElementId, i, monthValue, "report-express:sendFineByMonth:");
            setFeeMonthValue(freightList, orgElementId, i, monthValue, "report-express:freightByMonth:");
            setFeeMonthValue(income, orgElementId, i, monthValue, "report-express:incomeByMonth:");
        }
    }


    private void setFeeMonthValue(List<OptExpressMonthFeeVO> list, String orgElementId, int i, String monthValue, String s) {
        Object value = redisManager.getHashValue(s + orgElementId, monthValue);
        if (list.size() <= i) list.add(i, new OptExpressMonthFeeVO(orgElementId, monthValue, new BigDecimal("0")));
        if (value != null) list.get(i).setFee(list.get(i).getFee().add((BigDecimal) value));
    }

    // "统计物流状态"
    private void countStatus(Map<String, Object> result, boolean relation, List<SysOrgElementVO> orgList) {
        if (relation) {
            AtomicInteger n0 = new AtomicInteger(0);
            AtomicInteger n1 = new AtomicInteger(0);
            AtomicInteger n2 = new AtomicInteger(0);
            AtomicInteger n3 = new AtomicInteger(0);
            orgList.forEach(org -> {
                String orgElementId = org.getOrgElementId();
                Object value;
                value = redisManager.getHashValue("report-express:countStatus:" + orgElementId, "0");
                if (value != null) n0.addAndGet((int) value);
                value = redisManager.getHashValue("report-express:countStatus:" + orgElementId, "1");
                if (value != null) n1.addAndGet((int) value);
                value = redisManager.getHashValue("report-express:countStatus:" + orgElementId, "2");
                if (value != null) n2.addAndGet((int) value);
                value = redisManager.getHashValue("report-express:countStatus:" + orgElementId, "3");
                if (value != null) n3.addAndGet((int) value);
            });
            result.put("countStatus0", n0.get());
            result.put("countStatus1", n1.get());
            result.put("countStatus2", n2.get());
            result.put("countStatus3", n3.get());
        } else {
            String orgElementId = orgList.get(0).getOrgElementId();
            Object value = redisManager.getHashValue("report-express:" + orgElementId, "0");
            result.put("countStatus0", value != null ? value : 0);
            value = redisManager.getHashValue("report-express:" + orgElementId, "1");
            result.put("countStatus1", value != null ? value : 0);
            value = redisManager.getHashValue("report-express:" + orgElementId, "2");
            result.put("countStatus2", value != null ? value : 0);
            value = redisManager.getHashValue("report-express:" + orgElementId, "3");
            result.put("countStatus3", value != null ? value : 0);
        }
    }

    //"每月的问题件，退货件，每月的总件数统计"
    private void countFlagByMonth(Map<String, Object> result, boolean relation, List<SysOrgElementVO> orgList) {
        List<OptExpressMonthNumVO> questionList = new ArrayList<>(12);
        List<OptExpressMonthNumVO> bounceList = new ArrayList<>(12);
        List<OptExpressMonthNumVO> expressNumList = new ArrayList<>(12);
        result.put("questionByMonth", questionList);
        result.put("bounceByMonth", bounceList);
        result.put("expressNumByMonth", expressNumList);
        //是否关联下级数据
        if (relation) {
            orgList.forEach(org -> {
                String orgElementId = org.getOrgElementId();
                flagForMonth(questionList, bounceList, expressNumList, orgElementId);
            });
        } else {
            String orgElementId = orgList.get(0).getOrgElementId();
            flagForMonth(questionList, bounceList, expressNumList, orgElementId);
        }
    }

    private void flagForMonth(List<OptExpressMonthNumVO> questionList, List<OptExpressMonthNumVO> bounceList, List<OptExpressMonthNumVO> expressNumList, String orgElementId) {
        for (int i = 0; i < month.length; i++) {
            String monthValue = month[i];
            setFlagMonthValue(questionList, orgElementId, i, monthValue, "report-express:questionByMonth:");
            setFlagMonthValue(bounceList, orgElementId, i, monthValue, "report-express:bounceByMonth:");
            setFlagMonthValue(expressNumList, orgElementId, i, monthValue, "report-express:expressNumByMonth:");
        }
    }

    private void setFlagMonthValue(List<OptExpressMonthNumVO> list, String orgElementId, int i, String monthValue, String s) {
        Object value = redisManager.getHashValue(s + orgElementId, monthValue);
        if (list.size() <= i) list.add(i, new OptExpressMonthNumVO(orgElementId, monthValue, 0));
        if (value != null) list.get(i).setCount(list.get(i).getCount() + (int) value);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_REPORT_REFRESH','OPT_MANAGE_EXPRESS_ADMIN')")
    @ApiOperation("统计物流状态" +
            "省份寄件派送个数" +
            "每月的问题件，退货件，每月的总件数统计" +
            "每月的成本费（寄件代收货款手续费、到付手续费成本、中转费成本、面单成本），保费收入，运费，罚款，收入统计")
    @PostMapping("/refreshExport")
    public String refreshExport() {
        return getApi().refreshExport();
    }


}
