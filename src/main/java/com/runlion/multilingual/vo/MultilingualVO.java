/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.runlion.multilingual.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.runlion.multilingual.common.LongJsonDeserializer;
import com.runlion.multilingual.common.LongJsonSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author sixiaojie
 * @date 2019-12-18 10:53
 */
@Data
@Accessors(chain = true)
public class MultilingualVO {

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long id;

    /**
     * key
     */
    private String wordKey;

    /**
     * 源语言值
     */
    private String wordSourceValue;

    /**
     * 目标语言值
     */
    private String wordTargetValue;

    /**
     * 目标语言类型
     */
    private String wordTargetType;


    /**
     * 终端类型
     */
    private String clientType;

    /**
     * 文件标识（只适用于Android）
     */
    private String fileTag;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 是否删除
     */
    private Boolean deleted;

}
