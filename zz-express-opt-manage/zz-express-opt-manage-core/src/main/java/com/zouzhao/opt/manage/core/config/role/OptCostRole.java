
package com.zouzhao.opt.manage.core.config.role;


import com.zouzhao.common.security.annotation.AuthRole;
import com.zouzhao.common.security.annotation.AuthRoles;
import org.springframework.stereotype.Component;

/**
 * @author 姚超
 * @DATE: 2023-1-9
 * @DESCRIPTION: 系统
 */
@Component
@AuthRoles(
        roles = {
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_COST_DEFAULT",
                        messageKey = "运单管理:寄派件管理-默认权限",
                        desc = "拥有该基本权限才可以访问寄派件管理"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_COST_ADMIN",
                        messageKey = "运单管理:寄派件管理-管理员",
                        desc = "寄派件管理管理员"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_COST_LIST",
                        messageKey = "运单管理:寄派件管理-阅读所有",
                        desc = "允许阅读所有寄派件"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_COST_INSERT",
                        messageKey = "运单管理:寄派件管理-新增",
                        desc = "允许新增寄派件"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_COST_UPDATE",
                        messageKey = "运单管理:寄派件管理-更新",
                        desc = "允许更新寄派件"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_COST_DELETE",
                        messageKey = "运单管理:寄派件管理-删除",
                        desc = "允许删除寄派件"
                )
        }
)
public class OptCostRole {

}
