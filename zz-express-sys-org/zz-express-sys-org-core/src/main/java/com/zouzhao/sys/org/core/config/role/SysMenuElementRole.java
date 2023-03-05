package com.zouzhao.sys.org.core.config.role;


import com.zouzhao.sys.org.core.security.annotation.AuthRole;
import com.zouzhao.sys.org.core.security.annotation.AuthRoles;
import org.springframework.stereotype.Component;

/**
 * @author 姚超
 * @DATE: 2023-1-9
 * @DESCRIPTION: 菜单
 */
@Component
@AuthRoles(
        roles = {
                @AuthRole(
                        name = "ROLE_SYS_MENU_ELEMENT_DEFAULT",
                        messageKey = "菜单:菜单页面访问默认权限",
                        desc = "拥有该基本权限才可以访问菜单"
                ),
                @AuthRole(
                        name = "ROLE_SYS_MENU_ELEMENT_ADMIN",
                        messageKey = "菜单:菜单管理员",
                        desc = "菜单管理管理员"
                )
        }
)
public class SysMenuElementRole {

    public static final String ROLE_SYS_MENU_ELEMENT_DEFAULT = "ROLE_SYS_MENU_ELEMENT_DEFAULT";
    public static final String ROLE_SYS_MENU_ELEMENT_ADMIN = "ROLE_SYS_MENU_ELEMENT_ADMIN";

    public SysMenuElementRole() {
    }

}
