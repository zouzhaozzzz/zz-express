package com.zouzhao.express.opt.manage.core.service;

import com.zouzhao.common.service.PageServiceImpl;
import com.zouzhao.express.opt.manage.api.IOptExpressApi;
import com.zouzhao.express.opt.manage.core.entity.OptExpress;
import com.zouzhao.express.opt.manage.core.entity.OptExpressVO;
import com.zouzhao.express.opt.manage.core.mapper.OptExpressMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@Service
@RestController
@RequestMapping("/api/opt-manage/optExpress")
public class OptExpressService extends PageServiceImpl<OptExpressMapper, OptExpress, OptExpressVO> implements IOptExpressApi {



    @Override
    protected OptExpress voToEntity(OptExpressVO vo) {
        OptExpress express = new OptExpress();
        BeanUtils.copyProperties(vo,express);
        return express;
    }
}
