/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.runlion.multilingual.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sixiaojie
 * @date 2019-12-15 11:54
 */
@Getter
@AllArgsConstructor
public enum MultilingualClientTypeEnum {
    /**
     * 前端
     */
    FRONT(1,"PC"),

    /**
     * APP-IOS
     */
    APP_IOS(2,"APP-IOS"),
    /**
     * APP-Android
     */
    APP_ANDROID(3,"APP-Android"),
    /**
     * UNI-APP
     */
    UNI_APP(4,"UNI-APP"),
    /**
     * 后端-异常
     */
    BACK_END_MSG(5,"后端-异常"),
    /**
     * 后端-数据库中数据
     */
    BACK_END_ENUM(6,"后端-数据库中数据"),


    ;

    private Integer code;
    private String desc;

    public static MultilingualClientTypeEnum getClientTypeEnum(String desc){
        for (MultilingualClientTypeEnum value : MultilingualClientTypeEnum.values()) {
            if(value.desc.equals(desc)){
                return value;
            }
        }
        return null;
    }
}
