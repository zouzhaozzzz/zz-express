package com.zouzhao.common.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 姚超
 * @DATE: 2023-1-26
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRole {
    @AliasFor("value")
    String name() default "";

    @AliasFor("name")
    String value() default "";

    String messageKey();

    String desc() default "";


}
