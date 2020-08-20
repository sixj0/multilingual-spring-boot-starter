/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.runlion.multilingual.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常枚举
 * @author sixiaojie
 * @date 2019-12-23 11:29
 */
@Getter
@AllArgsConstructor
public enum BizMultilingualStatusEnum{
    /**
     * 200：操作成功
     */
    SUCCESS(200, "操作成功!!"),
    /**
     * 10000+：通用异常code
     */
    SYSTEM_ERROR(10000, "系统异常"),
    PARAM_ERROR(10001, "请求参数错误"),
    BIZ_ERROR(10002, "业务异常"),
    SYSTEM_BUSY_ERROR(10003, "系统繁忙，请稍后重试"),

    UPLOAD_FILE_ERROR(5001, "文件上传失败"),
    FILE_FORMAT_ERROR(5002, "上传文件格式不正确"),
    FILE_SIZE_MAX(5003, "上传文件过大"),

    PROJECT_IS_EMPTY(5114, "应用平台为空"),

    CLIENT_TYPE_IS_EMPTY(5309, "客户端类型为空"),


    WORD_TARGET_TYPE_IS_EMPTY(5401, "目标语言类型为空"),
    KEY_EXIST_EMPTY(5402, "key存在空"),
    MULTILINGUAL_ID_ERROR(5403, "多语言配置id错误"),
    FILE_TAG_IS_EMPTY(5404, "android类型的文件标识不能为空"),
    PROJECT_IS_ERROR(5405, "应用平台错误"),
    CLIENT_TYPE_IS_ERROR(5406, "客户端类型错误"),
    WORD_TARGET_TYPE_IS_ERROR(5407, "目标语言类型错误"),

    BAIDU_TRANSLATE_ERROR(5408,"百度翻译账号异常"),
    ;
    /**
     * 响应code
     */
    private Integer status;
    /**
     * 响应message
     */
    private String message;

    /**
     * 根据key值获取枚举类
     *
     * @param status 异常编号
     * @return respStatusEnum
     */
    public static BizMultilingualStatusEnum getEnumByKey(Integer status) {
        for (BizMultilingualStatusEnum respStatusEnum : BizMultilingualStatusEnum.values()) {
            if (respStatusEnum.getStatus().equals(status)) {
                return respStatusEnum;
            }
        }
        return SYSTEM_ERROR;
    }

}
