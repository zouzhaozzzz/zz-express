package com.zouzhao.opt.file.core.service.init;

import com.zouzhao.common.security.config.RoleAnnotationValidRegistrar;
import com.zouzhao.opt.file.api.init.IOptFileInit;
import com.zouzhao.sys.org.dto.SysRightRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
@RestController
@RequestMapping("/api/opt-file/optFileInit")
@Service
public class OptFileInitService implements IOptFileInit {

    @Override
    public List<Map<String, String>> roleConfig() {
        List<List<SysRightRoleVO>> roleLists = RoleAnnotationValidRegistrar.roleList;
        List<Map<String, String>> roles = new ArrayList<>();
        roleLists.forEach(roleVOList -> {
            roleVOList.forEach(vo ->
                    {
                        Map<String, String> map = new HashMap<>();
                        map.put("name",vo.getRightRoleName() );
                        map.put("code",vo.getRightRoleCode() );
                        map.put("module",vo.getRightRoleModule() );
                        map.put("desc",vo.getRightRoleDesc() );
                        roles.add(map);
                    }
            );
        });
        return roles;
    }
}
