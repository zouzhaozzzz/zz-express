package com.zouzhao.opt.manage.core.service;

import com.zouzhao.common.core.service.PageServiceImpl;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.core.entity.OptExpress;
import com.zouzhao.opt.manage.core.mapper.OptExpressMapper;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@Service
@RestController
@RequestMapping("/api/opt-manage/optExpress")
public class OptExpressService extends PageServiceImpl<OptExpressMapper, OptExpress, OptExpressVO> implements IOptExpressApi {

    private int batchSize;

    @Value("${export.batchSize}")
    public  void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }


    @Override
    protected OptExpress voToEntity(OptExpressVO vo) {
        OptExpress express = new OptExpress();
        BeanUtils.copyProperties(vo,express);
        return express;
    }

    @Override
    @Transactional
    public void batchSave(List<OptExpressVO> list) {
        List<OptExpress> data=new ArrayList<>();
        list.forEach(optExpressVO -> {
            OptExpress express = voToEntity(optExpressVO);
            data.add(express);
        });
        saveBatch(data,batchSize);
    }
}
