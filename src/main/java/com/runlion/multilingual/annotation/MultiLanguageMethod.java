package com.runlion.multilingual.annotation;

import com.runlion.multilingual.enums.MultilingualClientTypeEnum;

import java.lang.annotation.*;

/**
 * 多语言vo类注解
 *
 * @author wangyiting
 * @date 2019年12月31日 09:25:07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiLanguageMethod {

    /**
     * 多语言枚举类型
     *
     * @author wangyiting created 2020/1/2
     * @return multilingualClientTypeEnum
     */
    MultilingualClientTypeEnum multilingualClientTypeEnum() default MultilingualClientTypeEnum.BACK_END_ENUM;
}
