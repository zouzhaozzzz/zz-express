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
import com.zouzhao.opt.manage.dto.OptExpressNumVO;
import com.zouzhao.opt.manage.dto.OptExpressProvinceVO;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import com.zouzhao.sys.org.client.SysOrgElementClient;
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
    @Autowired
    private SysOrgElementClient sysOrgElementClient;

    private final String[] month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",};

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
        getMapper().pageQueryByCondition(vo, resultHandler);
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
    public Page<OptExpressVO> pagePlus(Page<OptExpressVO> page, List<SysOrgElementVO> orgList) {
        long current = page.getCurrent();
        long size = page.getSize();
        if (ObjectUtils.isEmpty(page.getRecords()) || ObjectUtils.isEmpty(page.getRecords().get(0)) || ObjectUtil.isEmpty(page.getRecords().get(0).getExpressStatusFlag())) {
            throw new MyException("相关信息传入不全");
        }
        OptExpressVO optExpressVO = page.getRecords().get(0);
        //是否差寄派件公司  1查寄件公司 2查派件公司
        Integer searchCompany = optExpressVO.getExpressStatusFlag();
        long total = page.getTotal();
        if (page.searchCount()) {
            total = getMapper().findCount(optExpressVO, searchCompany, orgList);
        }
        List<OptExpressVO> records;
        if (total == 0) {
            page.setCurrent(1);
            page.setTotal(0);
            page.setPages(0);
            records = new ArrayList<>();
        } else if (current * size > total) {
            //查询最后一页
            long newCurrent = total / size;
            if (newCurrent * size < total) {
                newCurrent++;
            }
            page.setCurrent(newCurrent);
            page.setTotal(total);
            page.setPages(newCurrent);
            records = getMapper().pagePlus((newCurrent - 1) * size, size, optExpressVO, searchCompany, orgList);
        } else {
            //查询当前页
            long newCurrent = total / size;
            if (newCurrent * size < total) {
                newCurrent++;
            }
            page.setTotal(total);
            page.setPages(newCurrent);
            records = getMapper().pagePlus((current - 1) * size, size, optExpressVO, searchCompany, orgList);
        }
        page.setRecords(records);
        return page;
    }


    @Override
    public String refreshExport() {
        //省份寄件派送个数
        countByProvinces();
        //统计快递状态，总件数统计
        countStatus();
        //每月的问题件,退货件,总件数
        countQuestionByMonth();
        countBounceByMonth();
        countExpressNumByMonth();

        //每月的成本费（寄件代收货款手续费、到付手续费成本、中转费成本、面单成本），保费收入，运费，罚款，收入统计
        countTotalCostByMonth();
        //保费收入=保费*0.6
        countPremiumByMonth();
        //运费
        countFreightByMonth();
        //罚款
        countSendFineByMonth();
        //收入=运费+罚款+保费收入-成本
        countIncomeByMouth();

        return "success";
    }

    //统计每月收入
    private void countIncomeByMouth() {
        //查询出所有组织
        SysOrgElementVO request = new SysOrgElementVO();
        request.setOrgElementType(0);
        request.setOrgElementStatus(true);
        List<SysOrgElementVO> orgList = sysOrgElementClient.findAll(request);
        orgList.forEach(org -> {
            for (int i = 0; i < month.length; i++) {
                String orgElementId = org.getOrgElementId();
                String currentMonth = month[i];
                Object premium = redisManager.getHashValue("report-express:premiumByMonth:" + orgElementId, currentMonth);
                Object freight = redisManager.getHashValue("report-express:freightByMonth:" + orgElementId, currentMonth);
                Object sendFine = redisManager.getHashValue("report-express:sendFineByMonth:" + orgElementId, currentMonth);
                Object totalCost = redisManager.getHashValue("report-express:totalCostByMonth:" + orgElementId, currentMonth);
                //如果为空设为0
                if (premium == null) premium = new BigDecimal("0");
                if (freight == null) freight = new BigDecimal("0");
                if (sendFine == null) sendFine = new BigDecimal("0");
                if (totalCost == null) totalCost = new BigDecimal("0");
                //每月收入
                BigDecimal income = ((BigDecimal) premium).add((BigDecimal) freight).add((BigDecimal) sendFine).subtract((BigDecimal) totalCost);
                redisManager.setHashValue("report-express:incomeByMonth" + orgElementId, currentMonth, income);
            }
        });
    }


    //统计每月的罚款
    private void countSendFineByMonth() {
        getMapper().countSendFineByMonth().forEach(
                e -> redisManager.setHashValue("report-express:sendFineByMonth:" + e.getName(), e.getMonth(), e.getFee())
        );
    }

    //统计每月的运费
    private void countFreightByMonth() {
        getMapper().countFreightByMonth().forEach(
                e -> redisManager.setHashValue("report-express:freightByMonth:" + e.getName(), e.getMonth(), e.getFee())
        );
    }

    //统计每月的保费收入 保费收入=保费*0.6
    private void countPremiumByMonth() {
        getMapper().countPremiumByMonth().forEach(
                e -> redisManager.setHashValue("report-express:premiumByMonth:" + e.getName(), e.getMonth(), e.getFee())
        );
    }

    //统计每月的总成本
    private void countTotalCostByMonth() {
        getMapper().countTotalCostByMonth().forEach(
                e -> redisManager.setHashValue("report-express:totalCostByMonth:" + e.getName(), e.getMonth(), e.getFee())
        );
    }

    //统计每月的总件数
    private void countExpressNumByMonth() {
        getMapper().countExpressNumByMonth().forEach(
                e -> redisManager.setHashValue("report-express:expressNumByMonth:" + e.getName(), e.getMonth(), e.getCount())
        );
    }

    //统计每月的退货件
    private void countBounceByMonth() {
        getMapper().countByBounce().forEach(
                e -> redisManager.setHashValue("report-express:bounceByMonth:" + e.getName(), e.getMonth(), e.getCount())
        );
    }

    //统计每月的问题件
    private void countQuestionByMonth() {
        getMapper().countByQuestion().forEach(
                e -> redisManager.setHashValue("report-express:questionByMonth:" + e.getName(), e.getMonth(), e.getCount())
        );
    }

    /**
     * 统计快递状态
     */
    private void countStatus() {
        //统计待取货，运输中，派件中，已签收
        getMapper().countByStatus(0).forEach(org -> redisManager.setHashValue("report-express:" + org.getName(), "0", org.getCount()));
        getMapper().countByStatus(1).forEach(org -> redisManager.setHashValue("report-express:" + org.getName(), "1", org.getCount()));
        getMapper().countByStatus(2).forEach(org -> redisManager.setHashValue("report-express:" + org.getName(), "2", org.getCount()));
        getMapper().countByStatus(3).forEach(org -> redisManager.setHashValue("report-express:" + org.getName(), "3", org.getCount()));
    }

    /**
     * 统计快递状态(统计当前登录人的组织和下级组织)
     */
    //统计快递状态
    // private List<Integer> countStatus(SysOrgElementVO parentOrg) {
    //     //统计待取货，运输中，派件中，已签收
    //     List<Integer> data = new ArrayList<>();
    //     int n1 = getMapper().countByStatus(0, parentOrg.getOrgElementId());
    //     int n2 = getMapper().countByStatus(1, parentOrg.getOrgElementId());
    //     int n3 = getMapper().countByStatus(2, parentOrg.getOrgElementId());
    //     int n4 = getMapper().countByStatus(3, parentOrg.getOrgElementId());
    //     data.add(n1);
    //     data.add(n2);
    //     data.add(n3);
    //     data.add(n4);
    //     List<SysOrgElementVO> children = sysOrgElementClient.findChildOrgById(parentOrg);
    //     children.forEach(child -> {
    //         List<Integer> list = countStatus(child);
    //         for (int i = 0; i < data.size(); i++) {
    //             data.set(i, data.get(i) + list.get(i));
    //         }
    //     });
    //     redisManager.setHashValue("report-express:" + parentOrg.getOrgElementName(), "0", data.get(0));
    //     redisManager.setHashValue("report-express:" + parentOrg.getOrgElementName(), "1", data.get(1));
    //     redisManager.setHashValue("report-express:" + parentOrg.getOrgElementName(), "2", data.get(2));
    //     redisManager.setHashValue("report-express:" + parentOrg.getOrgElementName(), "3", data.get(3));
    //     return data;
    // }

    //省份排名 寄件派送个数
    private void countByProvinces() {
        List<OptExpressProvinceVO> province = countNumByProvinces();
        redisManager.setHashValue("report-express", "province", province);
    }


    public List<OptExpressProvinceVO> countNumByProvinces() {
        List<OptExpressProvinceVO> result = new ArrayList<>();
        List<OptExpressNumVO> consigneeProvinces = getMapper().countByConsigneeProvinces();
        List<OptExpressNumVO> sendProvinces = getMapper().countBySendProvinces();
        if (ObjectUtil.isEmpty(consigneeProvinces)) {
            sendProvinces.forEach(e -> {
                String name = e.getName();
                int count = e.getCount();
                List<OptExpressNumVO> list = new ArrayList<>();
                list.add(new OptExpressNumVO("寄件", count));
                list.add(new OptExpressNumVO("派件", 0));
                result.add(new OptExpressProvinceVO(name, list));
            });
            return result;
        }
        if (ObjectUtil.isEmpty(sendProvinces)) {
            consigneeProvinces.forEach(e -> {
                String name = e.getName();
                int count = e.getCount();
                List<OptExpressNumVO> list = new ArrayList<>();
                list.add(new OptExpressNumVO("寄件", 0));
                list.add(new OptExpressNumVO("派件", count));
                result.add(new OptExpressProvinceVO(name, list));
            });
            return result;
        }
        Map<String, OptExpressNumVO> map = consigneeProvinces.stream().collect(Collectors.toMap(OptExpressNumVO::getName, item -> item));
        sendProvinces.forEach(e -> {
            String name = e.getName();
            int count = e.getCount();
            List<OptExpressNumVO> list = new ArrayList<>();
            list.add(new OptExpressNumVO("寄件", count));
            OptExpressNumVO consignee = map.get(name);
            if (consignee != null) {
                list.add(new OptExpressNumVO("派件", consignee.getCount()));
                map.remove(name);
            } else {
                list.add(new OptExpressNumVO("派件", 0));
            }
            result.add(new OptExpressProvinceVO(name, list));
        });
        //如果收货省份还有未添加的
        if (map.keySet().size() > 0) {
            map.values().forEach(e -> {
                String name = e.getName();
                int count = e.getCount();
                List<OptExpressNumVO> list = new ArrayList<>();
                list.add(new OptExpressNumVO("寄件", 0));
                list.add(new OptExpressNumVO("派件", count));
                result.add(new OptExpressProvinceVO(name, list));
            });
        }
        return result;
    }

    private void incrementNum(String redisKey, Integer size) {
        Object num = redisManager.getValue(redisKey);
        if (ObjectUtil.isEmpty(num)) {
            redisManager.setValue(redisKey, size);
        } else {
            int newNum = (int) num + size;
            redisManager.setValue(redisKey, newNum);
        }
    }

    //生成人员
    public List<SysOrgElementVO> randomPerson() {
        return getMapper().randomPerson();
    }
}
