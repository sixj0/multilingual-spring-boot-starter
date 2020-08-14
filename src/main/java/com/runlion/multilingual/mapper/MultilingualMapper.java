package com.runlion.multilingual.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runlion.multilingual.entity.Multilingual;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 多语言表 Mapper 接口
 * </p>
 *
 * @author sixiaojie
 * @since 2019-12-14
 */
public interface MultilingualMapper extends BaseMapper<Multilingual> {

    /**
     * 根据查询条件查询多语言数据
     *
     * @author wangyiting created 2019/12/31
     * @param languageType 语言类型
     * @param clientType 客户端类型
     * @param condition 条件
     * @return multilinguals
     */
    List<Multilingual> listMultilingual(@Param("languageType") String languageType, @Param("clientType") Integer clientType,
                                        @Param("condition") List<Multilingual> condition);

    /**
     * 根据查询条件查询多语言数据
     *
     * @author wangyiting created 2019/12/31
     * @param languageTypes 语言类型列表
     * @param clientType 客户端类型
     * @param condition 条件
     * @return multilinguals
     */
    List<Multilingual> listMultilingualForAllLanguageTypes(@Param("languageTypes") List<String> languageTypes, @Param("clientType") Integer clientType,
                                                           @Param("condition") List<Multilingual> condition);

}
