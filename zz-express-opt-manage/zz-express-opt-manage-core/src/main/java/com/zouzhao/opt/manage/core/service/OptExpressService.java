package com.zouzhao.opt.manage.core.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.common.core.service.PageServiceImpl;
import com.zouzhao.common.security.utils.RedisManager;
import com.zouzhao.opt.file.dto.OptExportConditionVO;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.core.entity.OptExpress;
import com.zouzhao.opt.manage.core.mapper.OptExpressMapper;
import com.zouzhao.opt.manage.dto.*;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@Service
@RestController
@RequestMapping("/api/opt-manage/optExpress")
public class OptExpressService extends PageServiceImpl<OptExpressMapper, OptExpress, OptExpressVO> implements IOptExpressApi {

    private static final Logger log = LoggerFactory.getLogger(OptExpressService.class);
    private int batchSize;
    @Autowired
    private RedisManager redisManager;

    @Value("${export.batchSize}")
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }


    @Override
    protected OptExpress voToEntity(OptExpressVO vo) {
        OptExpress express = new OptExpress();
        BeanUtils.copyProperties(vo, express);
        return express;
    }

    @Override
    @Transactional
    public void batchSave(String exportId, List<OptExpressVO> list) {
        List<OptExpress> data = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        //去重
        Set<String> idSet = new HashSet<>();
        list.stream().forEach(vo -> {
            String expressId = vo.getExpressId();
            //不存在该运单号的数据
            if (StrUtil.isBlank(expressId) || idSet.add(expressId)) {
                if (getMapper().isExistById(expressId) < 1) {
                    count.getAndIncrement();
                    OptExpress express = voToEntity(vo);
                    data.add(express);
                }
            }
        });
        //新增
        try {
            saveBatch(data, batchSize);
        } catch (Exception e) {
            redisManager.appendStrValue("import-err:" + exportId, e.getMessage());
            e.printStackTrace();
        }
        //在redis中放入成功数
        String redisKey = "import-success:" + exportId;
        if (count.get() > 0) incrementNum(redisKey, count.get());
    }

    @Override
    public void pageQueryByCondition(OptExportConditionVO vo, ResultHandler<OptExpressVO> resultHandler) {
        getMapper().pageQueryByCondition(vo,resultHandler);
    }


    @Override
    @Transactional
    public void updateStatusBatch(List<String> ids, Integer status) {
        getMapper().updateStatusBatch(ids, status);

    }

    @Override
    public int countExpressNum(OptExpressVO vo) {
        return getMapper().countExpressNum(vo);
    }

    @Override
    public Page<OptExpressVO> pagePlus(Page<OptExpressVO> page) {
        long current = page.getCurrent();
        long size = page.getSize();
        OptExpressVO optExpressVO = ObjectUtils.isEmpty(page.getRecords()) ? null : page.getRecords().get(0);
        long total=page.getTotal();
        if(page.searchCount()){
            total=getMapper().findCount(optExpressVO);
        }
        List<OptExpressVO> records;
        if(total == 0){
            page.setCurrent(1);
            page.setTotal(0);
            page.setPages(0);
            records=new ArrayList<>();
        }else if(current*size>total){
            //查询最后一页
            long newCurrent=total/size;
            if(newCurrent*size < total){newCurrent++;}
            page.setCurrent(newCurrent);
            page.setTotal(total);
            page.setPages(newCurrent);
            records=getMapper().pagePlus((newCurrent-1)*size,size,optExpressVO);
        }else{
            //查询当前页
            long newCurrent=total/size;
            if(newCurrent*size < total){newCurrent++;}
            page.setTotal(total);
            page.setPages(newCurrent);
            records=getMapper().pagePlus((current-1)*size,size,optExpressVO);
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public void refreshExport() {
        //统计快递状态，总件数统计
        countStatus();
        //省份寄件派送个数
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
        List<OptExpressMonthFeeVO> sendFineByMonth = getMapper().countSendFineByMonth();
        redisManager.setHashValue("report-express", "sendFineByMonth", sendFineByMonth);
        return sendFineByMonth;
    }

    //统计每月的运费
    private List<OptExpressMonthFeeVO> countFreightByMonth() {
        List<OptExpressMonthFeeVO> freightByMonth = getMapper().countFreightByMonth();
        redisManager.setHashValue("report-express", "freightByMonth", freightByMonth);
        return freightByMonth;
    }

    //统计每月的保费收入 保费收入=保费*0.6
    private List<OptExpressMonthFeeVO> countPremiumByMonth() {
        List<OptExpressMonthFeeVO> premiumByMonth = getMapper().countPremiumByMonth();
        premiumByMonth.forEach(e -> {
            if (e.getFee() != null) e.setFee(e.getFee().multiply(new BigDecimal("0.6")));
        });
        redisManager.setHashValue("report-express", "premiumByMonth", premiumByMonth);
        return premiumByMonth;
    }

    //统计每月的总成本
    private List<OptExpressMonthFeeVO> countTotalCostByMonth() {
        List<OptExpressMonthFeeVO> totalCostByMonth = getMapper().countTotalCostByMonth();
        redisManager.setHashValue("report-express", "totalCostByMonth", totalCostByMonth);
        return totalCostByMonth;
    }

    //统计每月的总件数
    private void countExpressNumByMonth() {
        List<OptExpressMonthNumVO> expressNumByMonth = getMapper().countExpressNumByMonth();
        redisManager.setHashValue("report-express", "expressNumByMonth", expressNumByMonth);
    }

    //统计每月的问题件
    private void countBounceByMonth() {
        List<OptExpressMonthNumVO> bounceByMonth = getMapper().countByBounce();
        redisManager.setHashValue("report-express", "bounceByMonth", bounceByMonth);
    }

    //统计每月的退货件
    private void countQuestionByMonth() {
        List<OptExpressMonthNumVO> questionNumByMonth = getMapper().countByQuestion();
        redisManager.setHashValue("report-express", "questionNumByMonth", questionNumByMonth);
    }

    //统计快递状态
    private void countStatus() {
        //统计待取货，运输中，派件中，已签收
        int n1 = getMapper().countByStatus(0);
        int n2 = getMapper().countByStatus(1);
        int n3 = getMapper().countByStatus(2);
        int n4 = getMapper().countByStatus(3);
        redisManager.setHashValue("report-express", "0", n1);
        redisManager.setHashValue("report-express", "1", n2);
        redisManager.setHashValue("report-express", "2", n3);
        redisManager.setHashValue("report-express", "3", n4);
        redisManager.setHashValue("report-express", "all", n1 + n2 + n3 + n4);
    }

    //省份排名 寄件派送个数
    private void countByProvinces() {
        List<OptExpressProvinceVO> province =countNumByProvinces();
        redisManager.setHashValue("report-express", "province", province);
    }


    public List<OptExpressProvinceVO> countNumByProvinces() {
        List<OptExpressProvinceVO> result=new ArrayList<>();
        List<OptExpressNumVO> consigneeProvinces = getMapper().countByConsigneeProvinces();
        List<OptExpressNumVO> sendProvinces = getMapper().countBySendProvinces();
        if(ObjectUtil.isEmpty(consigneeProvinces)){
            sendProvinces.forEach(e->{
                String name = e.getName();
                int count = e.getCount();
                List<OptExpressNumVO> list = new ArrayList<>();
                list.add(new OptExpressNumVO("寄件",count));
                list.add(new OptExpressNumVO("派件",0));
                result.add(new OptExpressProvinceVO(name,list));
            });
            return result;
        }
        if(ObjectUtil.isEmpty(sendProvinces)){
            consigneeProvinces.forEach(e->{
                String name = e.getName();
                int count = e.getCount();
                List<OptExpressNumVO> list = new ArrayList<>();
                list.add(new OptExpressNumVO("寄件",0));
                list.add(new OptExpressNumVO("派件",count));
                result.add(new OptExpressProvinceVO(name,list));
            });
            return result;
        }
        Map<String, OptExpressNumVO> map = consigneeProvinces.stream().collect(Collectors.toMap(OptExpressNumVO::getName, item -> item));
        sendProvinces.forEach(e->{
            String name = e.getName();
            int count = e.getCount();
            List<OptExpressNumVO> list = new ArrayList<>();
            list.add(new OptExpressNumVO("寄件",count));
            OptExpressNumVO consignee = map.get(name);
            if(consignee !=null){
                list.add(new OptExpressNumVO("派件",consignee.getCount()));
                map.remove(name);
            }else{
                list.add(new OptExpressNumVO("派件",0));
            }
            result.add(new OptExpressProvinceVO(name,list));
        });
        //如果收货省份还有未添加的
        if(map.keySet().size()>0){
            map.values().forEach(e->{
                String name = e.getName();
                int count = e.getCount();
                List<OptExpressNumVO> list = new ArrayList<>();
                list.add(new OptExpressNumVO("寄件",0));
                list.add(new OptExpressNumVO("派件",count));
                result.add(new OptExpressProvinceVO(name,list));
            });
        }
        return result;
    }

    private void incrementNum(String redisKey, Integer size) {
        Object num = redisManager.getValue(redisKey);
        if (ObjectUtil.isEmpty(num)) {
            redisManager.setValue(redisKey, size);
        } else {
            int newNum = (int)num + size;
            redisManager.setValue(redisKey,newNum);
        }
    }

    //生成人员
    public List<SysOrgElementVO> randomPerson() {
        return getMapper().randomPerson();
    }
}
