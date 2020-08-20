package com.sixj.multilingual.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sixiaojie
 * @date 2020-08-20-10:24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiLanguageEnum {

    /**
     * 需要支持国际化的异常枚举字段
     * @return
     */
    String fieldName();
}
