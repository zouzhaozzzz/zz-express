package com.zouzhao.sys.org.api;

import com.zouzhao.common.api.IApi;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-26
 */
public interface ISysRightRoleApi extends IApi<SysRightRoleVO> {

    @PostMapping("/allModule")
    List<String> allModule();
}
