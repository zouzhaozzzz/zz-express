package com.zouzhao.sys.org.api;

import com.zouzhao.common.api.IApi;
import com.zouzhao.sys.org.dto.SysMenuElementVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-24
 */
public interface ISysMenuElementApi extends IApi<SysMenuElementVO> {
    @PostMapping("/treeData")
    List<SysMenuElementVO> treeData(@RequestBody SysMenuElementVO vo);

    @PostMapping("/treeDataInRoles")
    List<SysMenuElementVO> treeDataInRoles(@RequestBody SysMenuElementVO vo);


    @PostMapping("/listInRoles")
    List<SysMenuElementVO> listInRoles(@RequestBody SysMenuElementVO vo);
}
