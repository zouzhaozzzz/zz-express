package com.zouzhao.sys.org.core.security.config;


import com.zouzhao.sys.org.core.entity.SysRightRole;
import com.zouzhao.sys.org.core.security.annotation.AuthRole;
import com.zouzhao.sys.org.core.security.annotation.AuthRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-26
 */
@Component
public class RoleAnnotationValidRegistrar implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(RoleAnnotationValidRegistrar.class);

    public static List<List<SysRightRole>> roleList=new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        AuthRoles annotation = AnnotationUtils.getAnnotation(bean.getClass(), AuthRoles.class);
        if (annotation != null) {
            AuthRole[] roles = annotation.roles();
            roleList.add(getRoles(roles)) ;
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    private List<SysRightRole> getRoles(AuthRole[] roles) {
        List<SysRightRole> rightRoles = new ArrayList<>();
        if (!ObjectUtils.isEmpty(roles)) {
            for (int i = 0; i < roles.length; i++) {
                AuthRole role = roles[i];
                SysRightRole sysRightRole = new SysRightRole();
                if (!ObjectUtils.isEmpty(role.name())) sysRightRole.setRightRoleCode(role.name());
                if (!ObjectUtils.isEmpty(role.messageKey())) {
                    String[] split = role.messageKey().split("[:：]");
                    if (split.length == 2) {
                        sysRightRole.setRightRoleModule(split[0]);
                        sysRightRole.setRightRoleName(split[1]);
                    }
                }
                if (!ObjectUtils.isEmpty(role.desc())) sysRightRole.setRightRoleDesc(role.desc());
                rightRoles.add(sysRightRole);
            }
        }
        return rightRoles;
    }
}
