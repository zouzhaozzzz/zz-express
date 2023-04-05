
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
                        name = "ROLE_OPT_MANAGE_REPORT_DEFAULT",
                        messageKey = "运营报表:运营报表-默认权限",
                        desc = "拥有该基本权限才可以访问运营报表"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_REPORT_ADMIN",
                        messageKey = "运营报表:运营报表-管理员",
                        desc = "运营报表管理员"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_REPORT_LIST",
                        messageKey = "运营报表:运营报表-阅读所有",
                        desc = "允许删除寄派件"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_REPORT_REFRESH",
                        messageKey = "运营报表:运营报表-刷新数据",
                        desc = "允许删除寄派件"
                )
        }
)
public class OptExportRole {

}
