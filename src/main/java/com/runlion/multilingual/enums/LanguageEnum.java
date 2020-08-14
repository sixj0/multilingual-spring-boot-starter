/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.runlion.multilingual.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sixiaojie
 * @date 2019-12-15 10:22
 */
@Getter
@AllArgsConstructor
public enum LanguageEnum {
    /**
     * 中文
     */
    ZH_CN("zh_cn"),

    /**
     * 英文
     */
    EN_US("en_us"),

    ;
    private String desc;

    public static LanguageEnum getLanguageEnum(String desc) {
        for (LanguageEnum state : LanguageEnum.values()) {
            if (state.desc.equals(desc)) {
                return state;
            }
        }
        return LanguageEnum.ZH_CN;
    }
}
