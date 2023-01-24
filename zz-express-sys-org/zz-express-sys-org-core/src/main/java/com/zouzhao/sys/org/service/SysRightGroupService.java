package com.zouzhao.sys.org.service;

import com.zouzhao.common.service.BaseServiceImpl;
import com.zouzhao.sys.org.api.ISysRightGroupApi;
import com.zouzhao.sys.org.dto.SysRightGroupVO;
import com.zouzhao.sys.org.entity.SysRightGroup;
import com.zouzhao.sys.org.mapper.SysRightGroupMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
@Service
@RestController
@RequestMapping("/api/sys-org/SysRightGroup")
public class SysRightGroupService extends BaseServiceImpl<SysRightGroupMapper, SysRightGroup, SysRightGroupVO> implements ISysRightGroupApi {


    @Override
    public SysRightGroup voToEntity(SysRightGroupVO vo) {
        SysRightGroup sysRightGroup = new SysRightGroup();
        BeanUtils.copyProperties(vo,sysRightGroup);
        return sysRightGroup;
    }
}
