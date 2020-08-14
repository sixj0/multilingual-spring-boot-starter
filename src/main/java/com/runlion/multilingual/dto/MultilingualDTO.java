/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.runlion.multilingual.dto;

import com.runlion.multilingual.enums.MultilingualClientTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author sixiaojie
 * @date 2019-12-23 20:28
 */
@Data
@Accessors(chain = true)
public class MultilingualDTO<T> implements Serializable {
    /**
     * 目标生成类对象
     */
    private Class<T> targetClass;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 需要翻译的表字段名
     */
    private String column;

    /**
     * 客户端类型枚举
     */
    private MultilingualClientTypeEnum multilingualClientTypeEnum;

    /**
     * 查询条件 字符串格式wordKey+"#"+word_source_value
     */
    List<String> conditions;

    /**
     * 被初始化的枚举类
     */
    private Class enumClass;
}
