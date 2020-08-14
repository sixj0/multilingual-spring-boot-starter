package com.runlion.multilingual.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 多语言表
 * </p>
 *
 * @author sixiaojie
 * @since 2019-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Multilingual implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 终端类型 1:前端;2:APP-IOS;3:APP-Android;4:UNI-APP;5:后端-异常；6：后端-数据库中数据
     */
    private Integer clientType;

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

    public Long getId() {
        return id;
    }

    public Multilingual() {
    }

    public Multilingual(String wordKey, String wordSourceValue, String wordTargetValue, String wordTargetType, Integer clientType, String fileTag) {
        this.wordKey = wordKey;
        this.wordSourceValue = wordSourceValue;
        this.wordTargetValue = wordTargetValue;
        this.wordTargetType = wordTargetType;
        this.clientType = clientType;
        this.fileTag = fileTag;
    }
}
