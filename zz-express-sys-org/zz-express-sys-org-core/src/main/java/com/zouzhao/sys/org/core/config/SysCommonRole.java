package com.zouzhao.sys.org.core.config;


import com.zouzhao.common.annotation.AuthRole;
import com.zouzhao.common.annotation.AuthRoles;
import org.springframework.stereotype.Component;

/**
 * @author 姚超
 * @DATE: 2023-1-9
 * @DESCRIPTION: 学生管理权限类
 */
@Component
@AuthRoles(
        roles = {
                @AuthRole(
                        name = "ROLE_DEFAULT",
                        messageKey = "系统:系统默认权限",
                        desc = "系统默认权限"
                ),
                @AuthRole(
                        name = "ROLE_PUBLISH",
                        messageKey = "系统:系统发布",
                        desc = "系统发布"
                ),
                @AuthRole(
                        name = "ROLE_MAINTAIN",
                        messageKey = "系统:系统维护",
                        desc = "系统维护"
                ),
                @AuthRole(
                        name = "ROLE_DELETE",
                        messageKey = "系统:系统删除",
                        desc = "系统删除"
                ),
                @AuthRole(
                        name = "ROLE_READ",
                        messageKey = "系统:系统阅读",
                        desc = "系统阅读"
                ),
                @AuthRole(
                        name = "ROLE_ADMIN",
                        messageKey = "系统:系统管理",
                        desc = "系统管理"
                )
        }
)
public class SysCommonRole {

    public static final String ROLE_DEFAULT = "ROLE_DEFAULT";
    public static final String ROLE_PUBLISH = "ROLE_PUBLISH";
    public static final String ROLE_MAINTAIN = "ROLE_MAINTAIN";
    public static final String ROLE_DELETE = "ROLE_DELETE";
    public static final String ROLE_READ = "ROLE_READ";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public SysCommonRole() {
    }

}
