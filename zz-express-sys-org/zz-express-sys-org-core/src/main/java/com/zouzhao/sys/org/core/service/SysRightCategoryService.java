package com.zouzhao.sys.org.core.service;

import com.zouzhao.common.service.PageServiceImpl;
import com.zouzhao.sys.org.api.ISysRightCategoryApi;
import com.zouzhao.sys.org.dto.SysRightCategoryVO;
import com.zouzhao.sys.org.core.entity.SysRightCategory;
import com.zouzhao.sys.org.core.mapper.SysRightCategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚超
 * @DATE: 2023-2-4
 */
@Service
@RestController
@RequestMapping("/api/sys-right/sysRightCategory")
public class SysRightCategoryService extends PageServiceImpl<SysRightCategoryMapper, SysRightCategory, SysRightCategoryVO> implements ISysRightCategoryApi {

    @Override
    public SysRightCategory voToEntity(SysRightCategoryVO vo) {
        SysRightCategory sysRightCategory = new SysRightCategory();
        BeanUtils.copyProperties(vo,sysRightCategory);
        return sysRightCategory;
    }


    // @Override
    // public Page<SysRightCategoryVO> page(Page<SysRightCategoryVO> page) {
    //     return getMapper().page(page,ObjectUtils.isEmpty(page.getRecords())?null:page.getRecords().get(0));
    // }


}
