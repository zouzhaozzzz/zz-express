
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
                        name = "ROLE_OPT_MANAGE_EXPRESS_DEFAULT",
                        messageKey = "运单管理:导入导出-默认权限",
                        desc = "拥有该基本权限才可以访问导入导出"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_EXPRESS_ADMIN",
                        messageKey = "运单管理:导入导出-管理员",
                        desc = "导入导出管理员"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_EXPRESS_LIST",
                        messageKey = "运单管理:导入导出-阅读所有",
                        desc = "允许阅读所有寄派件"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_EXPRESS_INSERT",
                        messageKey = "运单管理:导入导出-导入",
                        desc = "允许新增寄派件"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_EXPRESS_UPDATE",
                        messageKey = "运单管理:导入导出-导出",
                        desc = "允许更新寄派件"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_EXPRESS_DELETE",
                        messageKey = "运单管理:导入导出-删除",
                        desc = "允许删除寄派件"
                )
        }
)
public class OptExportRole {

}
