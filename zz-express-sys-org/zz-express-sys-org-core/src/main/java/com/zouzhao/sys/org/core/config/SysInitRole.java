package com.zouzhao.sys.org.core.config;


import com.zouzhao.common.annotation.AuthRole;
import com.zouzhao.common.annotation.AuthRoles;
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
                        name = "ROLE_SYS_DEFAULT",
                        messageKey = "系统:系统默认访问权限",
                        desc = "拥有该基本权限才可以访问系统管理"
                ),
                @AuthRole(
                        name = "ROLE_SYS_INIT",
                        messageKey = "系统:系统初始化",
                        desc = "对系统机制进行初始化"
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
public class SysInitRole {

    public static final String ROLE_SYS_INIT = "ROLE_SYS_INIT";

    public SysInitRole() {
    }

}
