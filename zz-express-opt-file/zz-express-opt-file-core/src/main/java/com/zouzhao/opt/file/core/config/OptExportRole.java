
package com.zouzhao.opt.file.core.config;


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
                        name = "ROLE_OPT_MANAGE_EXPORT_DEFAULT",
                        messageKey = "文件管理:导入导出-默认权限",
                        desc = "拥有该基本权限才可以访问导入导出"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_EXPORT_ADMIN",
                        messageKey = "文件管理:导入导出-管理员",
                        desc = "导入导出管理员"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_EXPORT_LIST",
                        messageKey = "文件管理:导入导出-阅读所有",
                        desc = "允许阅读所有导入导出"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_EXPORT_EXPORT",
                        messageKey = "文件管理:导入导出-导入",
                        desc = "允许新增导入导出"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_EXPORT_IMPORT",
                        messageKey = "文件管理:导入导出-导出",
                        desc = "允许更新导入导出"
                ),
        }
)
public class OptExportRole {

}
