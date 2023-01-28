package com.zouzhao.sys.org.controller.init;

import com.zouzhao.common.dto.IdNameDTO;
import com.zouzhao.common.dto.Response;
import com.zouzhao.sys.org.api.init.ISysInitApi;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SysInitController {
    @Autowired
    private ISysInitApi sysInitApi;

    @PostMapping("/init")
    public Response<List<IdNameDTO>> init() {
        return Response.ok(sysInitApi.init());
    }
}
