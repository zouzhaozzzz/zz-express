package com.zouzhao.sys.org.core.controller.init;

import com.zouzhao.common.dto.IdNameDTO;
import com.zouzhao.sys.org.api.init.ISysInitApi;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-26
 */
@RestController
@RequestMapping("/data/sys-org/sysInitController")
@Api(
        tags = "初始化接口"
)
@PreAuthorize("hasAnyRole('SYS_INIT')")
public class SysInitController {
    private static final Logger log = LoggerFactory.getLogger(SysInitController.class);
    @Autowired
    private ISysInitApi sysInitApi;

    @PostMapping("/init")
    public List<IdNameDTO> init() {
        return sysInitApi.init();
    }

    @PostMapping("/button")
    public List<String> button(){
        List<SysRightRoleVO> roles= (List<SysRightRoleVO>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        log.debug("roles:{}",roles);
        return null;
    }
}
