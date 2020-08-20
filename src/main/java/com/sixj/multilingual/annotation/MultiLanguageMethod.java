package com.sixj.multilingual.annotation;

import com.sixj.multilingual.common.DefaultDataGetter;
import com.sixj.multilingual.enums.MultilingualClientTypeEnum;

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

    /**
     * 获取复杂类型返回值中需要翻译的数据
     * @return
     */
     Class multiDataGetter() default DefaultDataGetter.class;
}
