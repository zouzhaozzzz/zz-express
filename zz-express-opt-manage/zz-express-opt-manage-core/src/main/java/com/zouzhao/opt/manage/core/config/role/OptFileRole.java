
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
                        name = "ROLE_OPT_MANAGE_FILE_UPLOAD",
                        messageKey = "文件管理:文件管理-上传",
                        desc = "拥有该基本权限才可以访问费用管理"
                ),
                @AuthRole(
                        name = "ROLE_OPT_MANAGE_FILE_DOWNLOAD",
                        messageKey = "文件管理:文件管理-下载",
                        desc = "费用管理管理员"
                ),
        }
)
public class OptFileRole {

}
