package com.zouzhao.sys.org.core.config;

import com.zouzhao.common.annotation.AuthRole;
import com.zouzhao.common.annotation.AuthRoles;
import org.springframework.stereotype.Component;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@Component
@AuthRoles(
        roles = {
                @AuthRole(
                        name = "ROLE_SYS_ORG_ELEMENT_DEFAULT",
                        messageKey = "组织架构:组织架构-默认权限",
                        desc = "拥有该基本权限才可以访问组织架构"
                ),
                @AuthRole(
                        name = "ROLE_SYS_ORG_ELEMENT_ADMIN",
                        messageKey = "组织架构:组织架构-管理员",
                        desc = "组织架构管理员"
                ),
                @AuthRole(
                        name = "ROLE_SYS_ORG_ELEMENT_LIST",
                        messageKey = "组织架构:组织架构-阅读所有",
                        desc = "允许阅读所有组织架构"
                ),
                @AuthRole(
                        name = "ROLE_SYS_ORG_ELEMENT_INSERT",
                        messageKey = "组织架构:组织架构-新增",
                        desc = "允许新增组织架构"
                ),
                @AuthRole(
                        name = "ROLE_SYS_ORG_ELEMENT_UPDATE",
                        messageKey = "组织架构:组织架构-更新",
                        desc = "允许更新组织架构"
                )
        }
)
public class SysOrgElementRole {
}
