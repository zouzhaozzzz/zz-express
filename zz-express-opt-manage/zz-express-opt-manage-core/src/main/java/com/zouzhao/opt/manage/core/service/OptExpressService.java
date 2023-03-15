package com.zouzhao.opt.manage.core.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public int countByStatus(Integer status) {
        return getMapper().countByStatus(status);
    }

    @Override
    public List<OptExpressProvinceVO> countByProvinces() {
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

    @Override
    public List<OptExpressMonthNumVO> countExpressNumByMonth() {
        return getMapper().countExpressNumByMonth();
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
