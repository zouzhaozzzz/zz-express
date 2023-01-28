package com.zouzhao.common.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 姚超
 * @DATE: 2023-1-26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRoles {
    @AliasFor("value")
    AuthRole[] roles() default {};

    @AliasFor("roles")
    AuthRole[] value() default {};
}
