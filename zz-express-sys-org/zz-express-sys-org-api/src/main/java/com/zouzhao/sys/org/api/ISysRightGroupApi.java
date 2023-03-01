package com.zouzhao.sys.org.api;

import com.zouzhao.common.api.IPageApi;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.sys.org.dto.SysRightGroupVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface ISysRightGroupApi extends IPageApi<SysRightGroupVO> {

    @PostMapping("/listPersonIdByAccount")
    @ApiOperation("查询角色附带分配的用户id")
    List<String> listPersonIdByAccount(IdDTO idDTO);

    @PostMapping("/allotToAccount")
    @ApiOperation("分配角色给账号")
    void allotToAccount(SysRightGroupVO vo);
}
