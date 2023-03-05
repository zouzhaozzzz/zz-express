package com.zouzhao.sys.org.core.config.role;


import com.zouzhao.sys.org.core.security.annotation.AuthRole;
import com.zouzhao.sys.org.core.security.annotation.AuthRoles;
import org.springframework.stereotype.Component;

/**
 * @author 姚超
 * @DATE: 2023-1-9
 * @DESCRIPTION: 权限管理
 */
@Component
@AuthRoles(
        roles = {
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_ROLE_DEFAULT",
                        messageKey = "权限管理:权限-默认权限",
                        desc = "拥有该基本权限才可以访问权限"
                ),
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_ROLE_LIST",
                        messageKey = "权限管理:权限-阅读所有",
                        desc = "允许阅读所有权限"
                ),

                @AuthRole(
                        name = "ROLE_SYS_RIGHT_GROUP_DEFAULT",
                        messageKey = "权限管理:角色-默认权限",
                        desc = "拥有该基本权限才可以访问角色"
                ),
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_GROUP_LIST",
                        messageKey = "权限管理:角色-阅读所有",
                        desc = "允许阅读所有角色"
                ),
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_GROUP_INSERT",
                        messageKey = "权限管理:角色-新增",
                        desc = "允许新增角色"
                ),
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_GROUP_UPDATE",
                        messageKey = "权限管理:角色-更新",
                        desc = "允许更新角色"
                ),
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_GROUP_DELETE",
                        messageKey = "权限管理:角色-删除",
                        desc = "允许删除角色"
                ),
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_GROUP_ALLOT",
                        messageKey = "权限管理:角色-分配",
                        desc = "允许分配角色给用户"
                ),

                @AuthRole(
                        name = "ROLE_SYS_RIGHT_CATEGORY_DEFAULT",
                        messageKey = "权限管理:角色类别-默认权限",
                        desc = "拥有该基本权限才可以访问角色类别"
                ),
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_CATEGORY_LIST",
                        messageKey = "权限管理:角色类别-阅读所有",
                        desc = "允许阅读所有角色类别"
                ),
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_CATEGORY_INSERT",
                        messageKey = "权限管理:角色类别-新增",
                        desc = "允许新增角色类别"
                ),
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_CATEGORY_UPDATE",
                        messageKey = "权限管理:角色类别-更新",
                        desc = "允许更新角色类别"
                ),
                @AuthRole(
                        name = "ROLE_SYS_RIGHT_CATEGORY_DELETE",
                        messageKey = "权限管理:角色类别-删除",
                        desc = "允许删除角色类别"
                ),
        }
)
public class SysRightRole {

    public static final String ROLE_SYS_RIGHT_ROLE_DEFAULT = "ROLE_SYS_RIGHT_ROLE_DEFAULT";
    public static final String ROLE_SYS_RIGHT_ROLE_LIST = "ROLE_SYS_RIGHT_ROLE_LIST";

    public static final String ROLE_SYS_RIGHT_GROUP_DEFAULT = "ROLE_SYS_RIGHT_GROUP_DEFAULT";
    public static final String ROLE_SYS_RIGHT_GROUP_LIST = "ROLE_SYS_RIGHT_GROUP_LIST";
    public static final String ROLE_SYS_RIGHT_GROUP_INSERT = "ROLE_SYS_RIGHT_GROUP_INSERT";
    public static final String ROLE_SYS_RIGHT_GROUP_UPDATE = "ROLE_SYS_RIGHT_GROUP_UPDATE";
    public static final String ROLE_SYS_RIGHT_GROUP_DELETE = "ROLE_SYS_RIGHT_GROUP_DELETE";
    public static final String ROLE_SYS_RIGHT_GROUP_ALLOT = "ROLE_SYS_RIGHT_GROUP_ALLOT";

    public static final String ROLE_SYS_RIGHT_CATEGORY_DEFAULT = "ROLE_SYS_RIGHT_CATEGORY_DEFAULT";
    public static final String ROLE_SYS_RIGHT_CATEGORY_LIST = "ROLE_SYS_RIGHT_CATEGORY_LIST";
    public static final String ROLE_SYS_RIGHT_CATEGORY_INSERT = "ROLE_SYS_RIGHT_CATEGORY_INSERT";
    public static final String ROLE_SYS_RIGHT_CATEGORY_UPDATE = "ROLE_SYS_RIGHT_CATEGORY_UPDATE";
    public static final String ROLE_SYS_RIGHT_CATEGORY_DELETE = "ROLE_SYS_RIGHT_CATEGORY_DELETE";


    public SysRightRole() {
    }

}
