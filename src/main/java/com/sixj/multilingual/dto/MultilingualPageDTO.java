/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.sixj.multilingual.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sixiaojie
 * @date 2019-12-18 10:55
 */
@Data
public class MultilingualPageDTO implements Serializable {
    /**
     * 分页
     */
    private Integer pageNum;
    private Integer pageSize;

    /**
     * 目标语言类型
     */
    private String wordTargetType;

    /**
     * 终端类型
     */
    private String clientType;
}
