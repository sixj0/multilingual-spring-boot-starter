package com.runlion.multilingual.service;

import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runlion.multilingual.dto.MultilingualDTO;
import com.runlion.multilingual.dto.MultilingualPageDTO;
import com.runlion.multilingual.dto.MultilingualParamDto;
import com.runlion.multilingual.entity.Multilingual;
import com.runlion.multilingual.vo.MultilingualVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 多语言表 服务类
 * </p>
 *
 * @author sixiaojie
 * @since 2019-12-14
 */
public interface MultilingualService extends IService<Multilingual> {
    /* ======================管理后台多语言配置======================== */


    /**
     * 导出Excel数据
     * @param clientType
     * @return
     */
    List<Map<String,Object>> createExcelData(Integer clientType);

    /**
     * 生成Excel
     * @param clientType
     * @return
     */
    ExcelWriter createExcel(Integer clientType);


    /**
     * 上传多语言配置Excel
     * @param file
     * @return
     */
    void uploadTemplate(MultipartFile file);

    /**
     * 分页列表展示
     * @param multilingualPageDTO
     * @return
     */
    IPage listPage(MultilingualPageDTO multilingualPageDTO);

    /**
     * 详情
     * @param id
     * @return
     */
    MultilingualVO detail(Long id);

    /**
     * 修改目标语言
     * @param id
     * @param targetValue
     * @return
     */
    void edit(Long id,String targetValue);

    /**
     * 删除
     * @param ids
     * @return
     */
    void delete(List<Long> ids);


    /**
     * 下载语言包
     * @param clientType
     * @param languageType
     * @return
     */
    List<String> downloadLangPackage(Integer clientType, String languageType);



    /**
     * 根据查询条件查询多语言数据
     *
     * @author wangyiting created 2019/12/31
     * @param multilingualDTO 多语言数据查询条件
     * @return multilingualVO
     */
    List<MultilingualVO> listMultilingual(MultilingualDTO multilingualDTO);


    /**
     * 新增数据库动态数据
     *
     * @author wangyiting created 2020/1/2
     * @param multilingualParamDto
     */
    void addMultilingualDb(MultilingualParamDto multilingualParamDto);


    /**
     * 同步异常枚举到多语言表
     * @param enumClass
     */
    void initEnumMessage(Class enumClass);

    /**
     * 启动项目时自动执行
     */
    void initEnumMessage();

    /**
     * 根据枚举名称查询对应的多语
     * @param enumClassName eEnum.getClassSimpleName()
     * @param enumName eEnum.name()
     * @param message 中文异常信息
     * @return
     */
    String getLanguageMessage(String enumClassName,String enumName,String message);


    /**
     * 转换枚举中的message
     * @param eEnum
     * @param <T>
     * @return
     */
    <T extends Enum> T getMultilingualEnum(T eEnum);
}
