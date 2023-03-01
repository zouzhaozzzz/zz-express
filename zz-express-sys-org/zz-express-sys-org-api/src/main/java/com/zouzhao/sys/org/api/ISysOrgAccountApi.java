package com.zouzhao.sys.org.api;

import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
public interface ISysOrgAccountApi extends IApi<SysOrgAccountVO> {

    @PostMapping("/checkLogin")
    String checkLogin(SysOrgAccountVO user);

    @PostMapping("/layout")
    String layout(SysOrgAccountVO user);


    SysOrgAccountVO findVOByDefPersonId(IdDTO of);

    List<String> getIdsByDefPersonIds(List<String> elementIds);


    void changePasswordByDefPerson(String accountDefPersonId, String password);
}
