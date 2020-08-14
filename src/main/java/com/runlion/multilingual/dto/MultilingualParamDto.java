package com.runlion.multilingual.dto;

import com.runlion.multilingual.enums.MultilingualClientTypeEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 多语言数据保存dto
 *
 * @author wangyiting
 * @version 1.0 created 2020/1/8
 */
@Data
public class MultilingualParamDto {

    /**
     * 多语言基础数据 map key=wordKey value=sourceValues
     */
    Map<String, List<String>> sourceMap;


    /**
     * 多语言客户端类型
     */
    MultilingualClientTypeEnum multilingualClientTypeEnum;
}
