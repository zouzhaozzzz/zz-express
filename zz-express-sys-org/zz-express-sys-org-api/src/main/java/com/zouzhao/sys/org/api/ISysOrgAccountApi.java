package com.zouzhao.sys.org.api;

import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
public interface ISysOrgAccountApi extends IApi<SysOrgAccountVO> {

    @PostMapping("/checkLogin")
    String checkLogin(@RequestBody SysOrgAccountVO user);

    @PostMapping("/layout")
    String layout(@RequestBody SysOrgAccountVO user);

    @PostMapping("/findVOByDefPersonId")
    SysOrgAccountVO findVOByDefPersonId(@RequestBody IdDTO of);

    @PostMapping("/getIdsByDefPersonIds")
    List<String> getIdsByDefPersonIds(@RequestBody List<String> elementIds);

    @PostMapping("/changePasswordByDefPerson")
    void changePasswordByDefPerson(@RequestBody SysOrgAccountVO sysOrgAccountVO);
}
