package com.zouzhao.opt.manage.core.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zouzhao.common.core.service.PageServiceImpl;
import com.zouzhao.common.security.utils.RedisManager;
import com.zouzhao.opt.file.dto.OptExportConditionVO;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.core.entity.OptExpress;
import com.zouzhao.opt.manage.core.mapper.OptExpressMapper;
import com.zouzhao.opt.manage.dto.OptExpressMonthFeeVO;
import com.zouzhao.opt.manage.dto.OptExpressMonthNumVO;
import com.zouzhao.opt.manage.dto.OptExpressProvinceVO;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    public int countByStatus(Integer status) {
        return getMapper().countByStatus(status);
    }

    @Override
    public List<OptExpressProvinceVO> countConsignByProvinces() {
        return getMapper().countConsignByProvinces();
    }

    @Override
    public List<OptExpressProvinceVO> countSendByProvinces() {
        return getMapper().countSendByProvinces();
    }

    @Override
    public List<OptExpressMonthNumVO> countBounceByMonth() {
        return getMapper().countByBounce();
    }

    @Override
    public List<OptExpressMonthNumVO> countQuestionByMonth() {
        return getMapper().countByQuestion();
    }

    @Override
    public List<OptExpressMonthFeeVO> countTotalCostByMonth() {
        return getMapper().countTotalCostByMonth();
    }

    @Override
    public List<OptExpressMonthFeeVO> countSendFineByMonth() {
        return getMapper().countSendFineByMonth();
    }

    @Override
    public List<OptExpressMonthFeeVO> countFreightByMonth() {
        return getMapper().countFreightByMonth();
    }

    @Override
    public List<OptExpressMonthFeeVO> countPremiumByMonth() {
        return getMapper().countPremiumByMonth();
    }

    @Override
    public int countExpressNum(OptExpressVO vo) {
        return getMapper().countExpressNum(vo);
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
}
