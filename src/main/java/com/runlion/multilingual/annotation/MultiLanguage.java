package com.runlion.multilingual.annotation;

import java.lang.annotation.*;

/**
 * 多语言vo类注解
 *
 * @author wangyiting
 * @date 2019年12月31日 09:25:07
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiLanguage {

    /**
     * 多语言字段注解
     *
     * @return 字段/类名注解
     */
    String name() default "" ;
}
