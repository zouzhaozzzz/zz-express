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
                        name = "ROLE_DEMOSTUDENT_DEFAULT",
                        messageKey = "demo-student:role.ROLE_DEMOSTUDENT_DEFAULT.name",
                        desc = "demo-student:role.ROLE_DEMOSTUDENT_DEFAULT.desc"
                ),
                @AuthRole(
                        name = "ROLE_DEMOSTUDENT_PUBLISH",
                        messageKey = "demo-student:role.ROLE_DEMOSTUDENT_PUBLISH.name",
                        desc = "demo-student:role.ROLE_DEMOSTUDENT_PUBLISH.desc"
                ),
                @AuthRole(
                        name = "ROLE_DEMOSTUDENT_MAINTAIN",
                        messageKey = "demo-student:role.ROLE_DEMOSTUDENT_MAINTAIN.name",
                        desc = "demo-student:role.ROLE_DEMOSTUDENT_MAINTAIN.desc"
                ),
                @AuthRole(
                        name = "ROLE_DEMOSTUDENT_ADMIN",
                        messageKey = "demo-student:role.ROLE_DEMOSTUDENT_ADMIN.name",
                        desc = "demo-student:role.ROLE_DEMOSTUDENT_ADMIN.desc"
                ),
                @AuthRole(
                        name = "ROLE_DEMOSTUDENT_SETTING",
                        messageKey = "demo-student:role.ROLE_DEMOSTUDENT_SETTING.name",
                        desc = "demo-student:role.ROLE_DEMOSTUDENT_SETTING.desc"
                ),
                @AuthRole(
                        name = "ROLE_DEMOSTUDENT_READ_ALL",
                        messageKey = "demo-student:role.ROLE_DEMOSTUDENT_READ_ALL.name",
                        desc = "demo-student:role.ROLE_DEMOSTUDENT_READ_ALL.desc"
                ),
                @AuthRole(
                        name = "ROLE_DEMOSTUDENT_CATEGORY_MAINTAINER",
                        messageKey = "demo-student:role.ROLE_DEMOSTUDENT_CATEGORY_MAINTAINER.name",
                        desc = "demo-student:role.ROLE_DEMOSTUDENT_CATEGORY_MAINTAINER.desc"
                ),
                @AuthRole(
                        name = "ROLE_DEMOSTUDENT_CATEGORY_ADMIN",
                        messageKey = "demo-student:role.ROLE_DEMOSTUDENT_CATEGORY_ADMIN.name",
                        desc = "demo-student:role.ROLE_DEMOSTUDENT_CATEGORY_ADMIN.desc"
                )
        }
)
public class SysOrgRole {

    public static final String ROLE_DEMOSTUDENT_DEFAULT = "ROLE_DEMOSTUDENT_DEFAULT";
    public static final String ROLE_DEMOSTUDENT_PUBLISH = "ROLE_DEMOSTUDENT_PUBLISH";
    public static final String ROLE_DEMOSTUDENT_MAINTAIN = "ROLE_DEMOSTUDENT_MAINTAIN";
    public static final String ROLE_DEMOSTUDENT_ADMIN = "ROLE_DEMOSTUDENT_ADMIN";
    public static final String ROLE_DEMOSTUDENT_SETTING = "ROLE_DEMOSTUDENT_SETTING";
    public static final String ROLE_DEMOSTUDENT_READ_ALL = "ROLE_DEMOSTUDENT_READ_ALL";
    public static final String ROLE_DEMOSTUDENT_CATEGORY_MAINTAINER = "ROLE_DEMOSTUDENT_CATEGORY_MAINTAINER";
    public static final String ROLE_DEMOSTUDENT_CATEGORY_ADMIN = "ROLE_DEMOSTUDENT_CATEGORY_ADMIN";
    public static final String CREATOR = "creator";


    public SysOrgRole() {
    }

}
