package com.zouzhao.opt.file.core.service;


import com.zouzhao.common.core.service.BaseServiceImpl;
import com.zouzhao.opt.file.api.IOptFileApi;
import com.zouzhao.opt.file.core.entity.OptFile;
import com.zouzhao.opt.file.core.mapper.OptFileMapper;
import com.zouzhao.opt.file.dto.OptFileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚超
 * @DATE: 2023-3-7
 */
@Service
@RestController
@RequestMapping("/api/opt-file/optFile")
public class OptFileService extends BaseServiceImpl<OptFileMapper, OptFile, OptFileVO> implements IOptFileApi {


    @Override
    protected OptFile voToEntity(OptFileVO vo) {
        OptFile file = new OptFile();
        BeanUtils.copyProperties(vo,file);
        return file;
    }


}
