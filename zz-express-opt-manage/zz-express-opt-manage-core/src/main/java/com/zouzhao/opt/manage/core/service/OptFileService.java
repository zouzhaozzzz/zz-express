package com.zouzhao.opt.manage.core.service;

import com.zouzhao.common.core.service.BaseServiceImpl;
import com.zouzhao.opt.manage.api.IOptFileApi;
import com.zouzhao.opt.manage.core.entity.OptFile;
import com.zouzhao.opt.manage.core.mapper.OptFileMapper;
import com.zouzhao.opt.manage.dto.OptFileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author 姚超
 * @DATE: 2023-3-7
 */
@Service
public class OptFileService extends BaseServiceImpl<OptFileMapper, OptFile, OptFileVO> implements IOptFileApi {


    @Override
    protected OptFile voToEntity(OptFileVO vo) {
        OptFile file = new OptFile();
        BeanUtils.copyProperties(vo,file);
        return file;
    }
}
