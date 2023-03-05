package com.zouzhao.sys.org.core.config.role;

import com.zouzhao.sys.org.core.security.annotation.AuthRole;
import com.zouzhao.sys.org.core.security.annotation.AuthRoles;
import org.springframework.stereotype.Component;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@Component
@AuthRoles(
        roles = {
                @AuthRole(
                        name = "ROLE_SYS_ORG_ACCOUNT_DEFAULT",
                        messageKey = "账号管理:账号管理-默认权限",
                        desc = "拥有该基本权限才可以访问账号管理"
                ),
        }
)
public class SysOrgAccountRole {
}
