package com.zouzhao.opt.manage.core.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.controller.PageController;
import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.common.security.utils.RedisManager;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.dto.OptExpressMonthFeeVO;
import com.zouzhao.opt.manage.dto.OptExpressMonthNumVO;
import com.zouzhao.opt.manage.dto.OptExpressProvinceVO;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import com.zouzhao.sys.org.client.SysOrgElementClient;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RedisManager redisManager;

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
        List<OptExpressVO> list = PageController.super.page(page).getRecords();
        //填充寄件客户，寄件公司
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
                    //所属公司
                    String sendCompanyId = expressVO.getSendCompanyId();
                    if (ObjectUtil.isNotEmpty(sendCompanyId)) {
                        SysOrgElementVO result = sysOrgElementClient.findVOById(IdDTO.of(sendCompanyId));
                        if (ObjectUtil.isNotEmpty(result)) expressVO.setSendCompany(result);
                    }
                }
        );
        return page;
    }

    @PostMapping("/countExpressNum")
    @ApiOperation("统计符合条件的快递数量")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_LIST','OPT_MANAGE_EXPRESS_ADMIN')")
    public int countExpressNum(@RequestBody OptExpressVO vo) {
        return getApi().countExpressNum(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_REPORT_LIST')")
    @ApiOperation("从redis中拿统计数据")
    @PostMapping("/export")
    public List<Map<String, Object>> export(@RequestBody List<String> keys) {
        List<Map<String, Object>> result = new ArrayList<>();
        keys.forEach(key -> {
            Map<String,Object> map=new HashMap<>();
            switch (key) {
                case "countStatus":
                    map.put("countStatus0", redisManager.getHashValue("report-express", "0"));
                    map.put("countStatus1", redisManager.getHashValue("report-express", "1"));
                    map.put("countStatus2", redisManager.getHashValue("report-express", "2"));
                    map.put("countStatus3", redisManager.getHashValue("report-express", "3"));
                    break;
                case "countByProvinces":
                    map.put("consignProvince",  redisManager.getHashValue("report-express", "consignProvince"));
                    map.put("sendProvince", redisManager.getHashValue("report-express", "sendProvince"));
                    break;
                case "countFlagByMonth":
                    map.put("questionNumByMonth",  redisManager.getHashValue("report-express", "questionNumByMonth"));
                    map.put("bounceByMonth", redisManager.getHashValue("report-express", "bounceByMonth"));
                    map.put("expressNumByMonth", redisManager.getHashValue("report-express", "expressNumByMonth"));
                    break;
                case "countFeeByMonth":
                    map.put("totalCostByMonth",  redisManager.getHashValue("report-express", "totalCostByMonth"));
                    map.put("premiumByMonth",  redisManager.getHashValue("report-express", "premiumByMonth"));
                    map.put("freightByMonth",  redisManager.getHashValue("report-express", "freightByMonth"));
                    map.put("sendFineByMonth",  redisManager.getHashValue("report-express", "sendFineByMonth"));
                    map.put("income",  redisManager.getHashValue("report-express", "income"));
                    break;
            }
            result.add(map);
        });
        return result;
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_REPORT_REFRESH')")
    @ApiOperation("统计快递状态" +
            "省份排名 寄件派送个数" +
            "每月的问题件，退货件，每月的总件数统计" +
            "每月的成本费（寄件代收货款手续费、到付手续费成本、中转费成本、面单成本），保费收入，运费，罚款，收入统计")
    @PostMapping("/refreshExport")
    public ResponseEntity<String> refreshExport() {
        //统计快递状态，总件数统计
        countStatus();
        //省份排名 寄件派送个数
        countByProvinces();
        //每月的问题件，退货件
        countQuestionByMonth();
        countBounceByMonth();
        countExpressNumByMonth();
        //每月的成本费（寄件代收货款手续费、到付手续费成本、中转费成本、面单成本），保费收入，运费，罚款，收入统计
        List<OptExpressMonthFeeVO> totalCost = countTotalCostByMonth();
        //保费收入=保费*0.6
        List<OptExpressMonthFeeVO> premium = countPremiumByMonth();
        //运费
        List<OptExpressMonthFeeVO> freight = countFreightByMonth();
        //罚款
        List<OptExpressMonthFeeVO> sendFine = countSendFineByMonth();
        //收入=运费+罚款+保费收入-成本
        countIncomeByMouth(totalCost, premium, freight, sendFine);
        return new ResponseEntity<>("刷新成功", HttpStatus.OK);
    }


    //统计每月收入
    private void countIncomeByMouth(List<OptExpressMonthFeeVO> totalCost, List<OptExpressMonthFeeVO> premium, List<OptExpressMonthFeeVO> freight, List<OptExpressMonthFeeVO> sendFine) {
        if (totalCost == null || premium == null || freight == null || sendFine == null)
            throw new MyException("计算收入出错，缺失数据");
        List<OptExpressMonthFeeVO> income = new ArrayList<>();
        for (int i = 0; i < totalCost.size(); i++) {
            OptExpressMonthFeeVO costVO = totalCost.get(i);
            BigDecimal premiumFee = premium.get(i).getFee();
            BigDecimal freightFee = freight.get(i).getFee();
            BigDecimal sendFineFee = sendFine.get(i).getFee();
            //如果为空设为0
            if (costVO.getFee() == null) costVO.setFee(new BigDecimal("0"));
            if (premiumFee == null) premiumFee = new BigDecimal("0");
            if (freightFee == null) freightFee = new BigDecimal("0");
            if (sendFineFee == null) sendFineFee = new BigDecimal("0");
            //每月收入
            BigDecimal incomeFee = premiumFee.add(freightFee).add(sendFineFee).subtract(costVO.getFee());
            costVO.setFee(incomeFee);
            income.add(costVO);
        }
        redisManager.setHashValue("report-express", "income", income);
    }


    //统计每月的罚款
    private List<OptExpressMonthFeeVO> countSendFineByMonth() {
        List<OptExpressMonthFeeVO> sendFineByMonth = getApi().countSendFineByMonth();
        redisManager.setHashValue("report-express", "sendFineByMonth", sendFineByMonth);
        return sendFineByMonth;
    }

    //统计每月的运费
    private List<OptExpressMonthFeeVO> countFreightByMonth() {
        List<OptExpressMonthFeeVO> freightByMonth = getApi().countFreightByMonth();
        redisManager.setHashValue("report-express", "freightByMonth", freightByMonth);
        return freightByMonth;
    }

    //统计每月的保费收入 保费收入=保费*0.6
    private List<OptExpressMonthFeeVO> countPremiumByMonth() {
        List<OptExpressMonthFeeVO> premiumByMonth = getApi().countPremiumByMonth();
        premiumByMonth.forEach(e -> {
            if (e.getFee() != null) e.setFee(e.getFee().multiply(new BigDecimal("0.6")));
        });
        redisManager.setHashValue("report-express", "premiumByMonth", premiumByMonth);
        return premiumByMonth;
    }

    //统计每月的总成本
    private List<OptExpressMonthFeeVO> countTotalCostByMonth() {
        List<OptExpressMonthFeeVO> totalCostByMonth = getApi().countTotalCostByMonth();
        redisManager.setHashValue("report-express", "totalCostByMonth", totalCostByMonth);
        return totalCostByMonth;
    }

    //统计每月的总件数
    private void countExpressNumByMonth() {
        List<OptExpressMonthNumVO> expressNumByMonth = getApi().countExpressNumByMonth();
        redisManager.setHashValue("report-express", "expressNumByMonth", expressNumByMonth);
    }

    //统计每月的问题件
    private void countBounceByMonth() {
        List<OptExpressMonthNumVO> bounceByMonth = getApi().countBounceByMonth();
        redisManager.setHashValue("report-express", "bounceByMonth", bounceByMonth);
    }

    //统计每月的退货件
    private void countQuestionByMonth() {
        List<OptExpressMonthNumVO> questionNumByMonth = getApi().countQuestionByMonth();
        redisManager.setHashValue("report-express", "questionNumByMonth", questionNumByMonth);
    }

    //统计快递状态
    private void countStatus() {
        //统计待取货，运输中，派件中，已签收
        int n1 = getApi().countByStatus(0);
        int n2 = getApi().countByStatus(1);
        int n3 = getApi().countByStatus(2);
        int n4 = getApi().countByStatus(3);
        redisManager.setHashValue("report-express", "0", n1);
        redisManager.setHashValue("report-express", "1", n2);
        redisManager.setHashValue("report-express", "2", n3);
        redisManager.setHashValue("report-express", "3", n4);
        redisManager.setHashValue("report-express", "all", n1 + n2 + n3 + n4);
    }

    //省份排名 寄件派送个数
    private void countByProvinces() {
        List<OptExpressProvinceVO> consign = getApi().countConsignByProvinces();
        List<OptExpressProvinceVO> send = getApi().countSendByProvinces();
        redisManager.setHashValue("report-express", "consignProvince", consign);
        redisManager.setHashValue("report-express", "sendProvince", send);
    }

}
