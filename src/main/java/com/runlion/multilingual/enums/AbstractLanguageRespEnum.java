package com.runlion.multilingual.enums;

/**
 * @author sixiaojie
 * @date 2020-02-20-14:32
 */
public interface AbstractLanguageRespEnum {
    /**
     * 获取状态码
     * @return
     */
    Integer getStatus();

    /**
     * 获取异常信息
     * @return
     */
    String getMessage();

    /**
     * 获取枚举类名
     * @return
     */
    String getClassSimpleName();

    /**
     * 获取具体枚举名
     * @return
     */
    String name();


}
