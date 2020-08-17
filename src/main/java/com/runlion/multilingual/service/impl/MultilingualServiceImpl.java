package com.runlion.multilingual.service.impl;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runlion.multilingual.dto.MultilingualDTO;
import com.runlion.multilingual.dto.MultilingualPageDTO;
import com.runlion.multilingual.dto.MultilingualParamDto;
import com.runlion.multilingual.entity.Multilingual;
import com.runlion.multilingual.enums.AbstractLanguageRespEnum;
import com.runlion.multilingual.enums.BizMultilingualStatusEnum;
import com.runlion.multilingual.enums.LanguageEnum;
import com.runlion.multilingual.enums.MultilingualClientTypeEnum;
import com.runlion.multilingual.exception.MultilingualException;
import com.runlion.multilingual.mapper.MultilingualMapper;
import com.runlion.multilingual.service.MultilingualService;
import com.runlion.multilingual.translater.baidu.TransApi;
import com.runlion.multilingual.utils.LanguageInfoUtil;
import com.runlion.multilingual.utils.MultilingualJsonUtil;
import com.runlion.multilingual.utils.MultilingualBeanUtil;
import com.runlion.multilingual.vo.MultilingualVO;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 多语言表 服务实现类
 * </p>
 *
 * @author sixiaojie
 * @since 2019-12-14
 */
@Service
public class MultilingualServiceImpl extends ServiceImpl<MultilingualMapper, Multilingual> implements MultilingualService {
    private static final String SEPARATOR = ".";
    private static final String UNDERLINE = "_";
    private static final String REGEX_XLS = "^.+\\.(?i)(xls)$";
    private static final String REGEX_XLSX = "^.+\\.(?i)(xlsx)$";

    /** 扫描异常枚举类,多个包可用逗号分割 */
    @Value("${language.scanner.packages:com}")
    private String packages;

    /** 系统需要使用的外语，多种外语可用逗号分割 */
    @Value("${language.system.used:en}")
    private String systemUsed;

    /** 是否启用自动翻译 */
    @Value("${language.autoTranslate.enable:false}")
    private Boolean enableAutoTranslate;

    /** 百度翻译APP ID */
    @Value("${baidu.translate.appId}")
    private String appId;

    /** 百度翻译密钥 */
    @Value("${baidu.translte.securityKey}")
    private String securityKey;



    @Resource
    private MultilingualMapper multilingualMapper;

    @Override
    public void uploadTemplate(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (!fileName.matches(REGEX_XLS) && !fileName.matches(REGEX_XLSX)){
            throw new MultilingualException(BizMultilingualStatusEnum.FILE_FORMAT_ERROR);
        }
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        }catch (IOException e){
            throw new MultilingualException(BizMultilingualStatusEnum.SYSTEM_ERROR);
        }
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        // 客户端类型
        String clientType = objectToStr(reader.readCellValue(1, 0));
        if(StringUtils.isEmpty(clientType)){
            throw new MultilingualException(BizMultilingualStatusEnum.CLIENT_TYPE_IS_EMPTY);
        }
        MultilingualClientTypeEnum clientTypeEnum = MultilingualClientTypeEnum.getClientTypeEnum(clientType);
        if(Objects.isNull(clientTypeEnum)){
            throw new MultilingualException(BizMultilingualStatusEnum.CLIENT_TYPE_IS_ERROR);
        }
        // 目标语言类型
        String wordTargetType = objectToStr(reader.readCellValue(1, 1));
        if(StringUtils.isEmpty(wordTargetType)){
            throw new MultilingualException(BizMultilingualStatusEnum.WORD_TARGET_TYPE_IS_EMPTY);
        }
        LanguageEnum languageEnum = LanguageEnum.getLanguageEnum(wordTargetType);
        if(Objects.isNull(languageEnum)){
            throw new MultilingualException(BizMultilingualStatusEnum.WORD_TARGET_TYPE_IS_ERROR);
        }
        // 文件标识
        String fileTag = objectToStr(reader.readCellValue(1,2));
        if(MultilingualClientTypeEnum.APP_ANDROID.getDesc().equals(clientType) &&
                StringUtils.isEmpty(fileTag)){
            throw new MultilingualException(BizMultilingualStatusEnum.FILE_TAG_IS_EMPTY);
        }
        // 读取内容
        List<Multilingual> multilingualList = new ArrayList<>();
        boolean loopCondition = true;
        for(int i = 4 ; loopCondition ;i++){
            String wordKey = objectToStr(reader.readCellValue(0, i));
            if(StringUtils.isEmpty(wordKey)){
                throw new MultilingualException("上传失败：第"+(i+1)+"行key为空，请检查后重新上传");
            }
            String wordSourceValue = objectToStr(reader.readCellValue(1, i));
            // 源语言值不能为空
            if(StringUtils.isEmpty(wordSourceValue)){
                throw new MultilingualException("上传失败：第"+(i+1)+"行源语言值为空，请检查后重新上传");
            }
            String wordTargetValue = objectToStr(reader.readCellValue(2, i));
            Multilingual multilingual = new Multilingual(wordKey, wordSourceValue, wordTargetValue, wordTargetType, clientTypeEnum.getCode(), fileTag);
            multilingualList.add(multilingual);
            // 只要下一行不是空行就循环读取
            List<Object> objects = reader.readRow(i + 1);
            if(CollectionUtils.isEmpty(objects)){
                loopCondition = false;
            }
        }
        // key不能重复
        List<String> distinctCollect = multilingualList.stream().map(Multilingual::getWordKey).distinct().collect(Collectors.toList());
        if(distinctCollect.size() < multilingualList.size()){
            throw new MultilingualException("上传失败：key不能重复，请检查后重新上传");
        }

        // 查询原有数据，根据key是否存在判断新增或者修改
        QueryWrapper<Multilingual> multilingualQueryWrapper = new QueryWrapper<>();
        multilingualQueryWrapper
                .eq("client_type",clientType)
                .eq("word_target_type",wordTargetType)
                .eq("deleted",false);
        if(!StringUtils.isEmpty(fileTag)){
            multilingualQueryWrapper.eq("file_tag",fileTag);
        }
        List<Multilingual> multilingualDbList = this.list(multilingualQueryWrapper);
        List<String> keyList = multilingualDbList.stream().map(Multilingual::getWordKey).collect(Collectors.toList());

        // 修改条件
        UpdateWrapper<Multilingual> multilingualUpdateWrapper = new UpdateWrapper<>();
        multilingualUpdateWrapper
                .eq("client_type",clientType)
                .eq("word_target_type",wordTargetType);
        List<Multilingual> saveList = new ArrayList<>();

        // 新增或修改
        for (Multilingual multilingual : multilingualList) {
            if(keyList.contains(multilingual.getWordKey())){
                multilingualUpdateWrapper.eq("word_key",multilingual.getWordKey());
                this.update(multilingual,multilingualUpdateWrapper);
            }else{
                saveList.add(multilingual);
            }
        }
        if(!CollectionUtils.isEmpty(saveList)){
            this.saveBatch(saveList);
        }
    }

    private String objectToStr(Object o){
        if(o == null){
            return "";
        }
        return String.valueOf(o);
    }
    @Override
    public IPage listPage(MultilingualPageDTO dto) {
        // 默认值
        if(StringUtils.isEmpty(dto.getWordTargetType())){
            dto.setWordTargetType(LanguageEnum.EN.getCode());
        }

        if(Objects.isNull(dto.getClientType())){
            dto.setClientType(MultilingualClientTypeEnum.FRONT.getDesc());
        }
        QueryWrapper<Multilingual> multilingualQueryWrapper = new QueryWrapper<>();
        multilingualQueryWrapper.eq("word_target_type",dto.getWordTargetType())
                .eq("client_type",dto.getClientType())
                .eq("deleted",false)
                .orderByDesc("gmt_create");
        IPage<Multilingual> multilingualPage =
                this.page(new Page<>(dto.getPageNum(), dto.getPageSize()), multilingualQueryWrapper);
        List<Multilingual> multilingualList = multilingualPage.getRecords();
        if(CollectionUtils.isEmpty(multilingualList)){
            return multilingualPage;
        }
        List<MultilingualVO> multilingualVos = MultilingualBeanUtil.copyListProperties(multilingualList, MultilingualVO.class);
        Page<MultilingualVO> multilingualVoPage = new Page<>();
        BeanUtils.copyProperties(multilingualPage,multilingualVoPage);
        multilingualVoPage.setRecords(multilingualVos);
        return multilingualVoPage;
    }

    @Override
    public MultilingualVO detail(Long id) {
        Multilingual multilingual = this.getById(id);
        if(Objects.isNull(multilingual)){
            throw new MultilingualException(BizMultilingualStatusEnum.MULTILINGUAL_ID_ERROR);
        }
        MultilingualVO multilingualVO = new MultilingualVO();
        BeanUtils.copyProperties(multilingual,multilingualVO);
        return multilingualVO;
    }

    @Override
    public void edit(Long id, String targetValue) {
        Multilingual multilingual = new Multilingual();
        multilingual.setId(id).setWordTargetValue(targetValue);
        this.updateById(multilingual);
    }

    @Override
    public void delete(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            throw new MultilingualException(BizMultilingualStatusEnum.MULTILINGUAL_ID_ERROR);
        }
        List<Multilingual> multilingualList = new ArrayList<>();
        for (Long id : ids) {
            Multilingual multilingual = new Multilingual();
            multilingual.setId(id).setDeleted(true);
            multilingualList.add(multilingual);
        }
        this.updateBatchById(multilingualList);
    }

    @Override
    public List<String> downloadLangPackage(Integer clientType, String languageType) {
        QueryWrapper<Multilingual> multilingualQueryWrapper = new QueryWrapper<>();
        multilingualQueryWrapper.eq("word_target_type",languageType)
                .eq("client_type",clientType)
                .eq("deleted",false);
        List<Multilingual> multilingualList = this.list(multilingualQueryWrapper);
        List<String> resultList = new ArrayList<>();
        // 前端:JSON格式文件
        if(MultilingualClientTypeEnum.FRONT.getCode().equals(clientType)){
            HashMap<String, String> map = new HashMap<>(16);
            for (Multilingual multilingual : multilingualList) {
                // 如果没有对应的目标语言，依然返回中文
                String wordTargetValue = multilingual.getWordTargetValue();
                String value = StringUtils.isEmpty(wordTargetValue) ? multilingual.getWordSourceValue() : wordTargetValue;
                map.put(multilingual.getWordKey(),value);
            }
            // 格式化json字符串
            String formatJson = MultilingualJsonUtil.formatJson(JSONUtil.toJsonStr(map));
            resultList.add(formatJson);
        }
        // Android：<string name="key">value</string>
        if(MultilingualClientTypeEnum.APP_ANDROID.getCode().equals(clientType)){
            // 按照文件标识排序，按顺序导出
            multilingualList.sort(Comparator.comparing(Multilingual::getFileTag));
            for (Multilingual multilingual : multilingualList) {
                // 如果没有对应的目标语言，依然返回中文
                String wordTargetValue = multilingual.getWordTargetValue();
                String value = StringUtils.isEmpty(wordTargetValue) ? multilingual.getWordSourceValue() : wordTargetValue;
                resultList.add("<string name=\""+multilingual.getWordKey()+"\">"+value+"</string>");
            }
        }
        // IOS：key = "value";
        if(MultilingualClientTypeEnum.APP_IOS.getCode().equals(clientType)){
            for (Multilingual multilingual : multilingualList) {
                // 如果没有对应的目标语言，依然返回中文
                String wordTargetValue = multilingual.getWordTargetValue();
                String value = StringUtils.isEmpty(wordTargetValue) ? multilingual.getWordSourceValue() : wordTargetValue;
                resultList.add(multilingual.getWordKey()+" = \""+value+"\";");
            }
        }
        return resultList;
    }


    @Override
    public List<MultilingualVO> listMultilingual(MultilingualDTO multilingualDTO) {
        String languageEnum = LanguageInfoUtil.getCurrentLanguage();
        //客户端类型以及查询条件，其中一个为空则返回null
        if (multilingualDTO.getMultilingualClientTypeEnum() == null
                || CollectionUtils.isEmpty(multilingualDTO.getConditions())) {
            return null;
        }
        List<Multilingual> conditions = new ArrayList<>();
        for (Object o : multilingualDTO.getConditions()) {
            String condition = String.valueOf(o);
            String[] conditionArr = condition.split("#");
            Multilingual multilingual = new Multilingual();
            multilingual.setWordKey(conditionArr[0]).setWordSourceValue(conditionArr[1]);
            conditions.add(multilingual);
        }
        //根据条件查询多语言数据
        List<Multilingual> multilinguals = multilingualMapper.listMultilingual(languageEnum
                , multilingualDTO.getMultilingualClientTypeEnum().getCode(), conditions);
        return MultilingualBeanUtil.copyListProperties(multilinguals, MultilingualVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMultilingualDb(MultilingualParamDto multilingualParamDto) {
        Map<String, List<String>> sourceMap = multilingualParamDto.getSourceMap();
        MultilingualClientTypeEnum multilingualClientTypeEnum = multilingualParamDto.getMultilingualClientTypeEnum();
        //所有语言类型(去除中文)
        List<String> languageCodeList = StrUtil.split(systemUsed, ',', true, true);
        List<String> languageTypes = languageCodeList.stream().filter(language -> !language.equals(LanguageEnum.ZH.getCode()))
                .collect(Collectors.toList());
        List<Multilingual> multilingualList = new ArrayList<>();
        List<Multilingual> params = new ArrayList<>();
        sourceMap.forEach((key, value) -> value.forEach(source -> {
            languageTypes.forEach(type -> {
                Multilingual multilingual = new Multilingual();
                multilingual.setWordKey(String.valueOf(key))
                        .setWordSourceValue(source)
                        .setWordTargetType(type)
                        .setClientType(multilingualClientTypeEnum.getCode());
                multilingualList.add(multilingual);
            });
            //设置查询条件
            Multilingual multilingualQuery = new Multilingual();
            multilingualQuery.setWordKey(key);
            multilingualQuery.setWordSourceValue(source);
            params.add(multilingualQuery);
        }));
        //根据条件查询多语言数据
        List<Multilingual> multilinguals = multilingualMapper.listMultilingualForAllLanguageTypes(languageTypes, multilingualClientTypeEnum.getCode(), params);
        //存在相同数据需要剔除
        if (!CollectionUtils.isEmpty(multilinguals)) {
            //将多语言数据组合成string用以对比
            List<String> multilingualStrs = multilinguals.parallelStream().map(this::getString).collect(Collectors.toList());
            //删除已存在的多语言数据
            multilingualList.removeIf(multilingual -> multilingualStrs.contains(this.getString(multilingual)));
        }
        this.saveBatch(multilingualList);

        // 自动百度翻译
        if(enableAutoTranslate){
            if(StrUtil.isBlank(appId) || StrUtil.isBlank(securityKey)){
                throw new MultilingualException(BizMultilingualStatusEnum.BAIDU_TRANSLATE_ERROR);
            }
            new Thread(()->{
                List<Multilingual> needTrans = multilingualList.stream().filter(multilingual -> LanguageEnum.getLanguageEnum(multilingual.getWordTargetType()).getBaiduEnable())
                        .collect(Collectors.toList());
                int count = needTrans.size();
                // 由于百度API的并发限制，可能会漏掉，所以使用while
                while (count != 0){
                    for (Multilingual multilingual : multilingualList) {
                        String type = multilingual.getWordTargetType();
                        if(StrUtil.isBlank(multilingual.getWordTargetValue())){
                            // 翻译
                            TransApi baiduApi = new TransApi(appId, securityKey);
                            String wordTargetValue = baiduApi.getTransResult(multilingual.getWordSourceValue(), "auto", type);
                            // 将翻译后的值写入数据库
                            multilingual.setWordTargetValue(wordTargetValue);
                            count --;
                        }
                    }
                }
                updateBatchById(multilingualList);
            }).start();
        }
    }

    /**
     * 组合多语言数据为string
     *
     * @author wangyiting created 2020/1/10
     * @param multilingual 多语言数据
     * @return string
     */
    private String getString(Multilingual multilingual) {
        return multilingual.getClientType()
                + "#" + multilingual.getWordTargetType()
                + "#" + multilingual.getWordKey()
                + "#" + multilingual.getWordSourceValue();
    }

    @Override
    @PostConstruct
    public void initEnumMessage(){
        new Thread(()->{
            List<String> list = StrUtil.split(packages, ',', true, true);
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .forPackages(list.toArray(new String[0]))
                    .addScanners(new SubTypesScanner())
            );
            Set<Class<? extends AbstractLanguageRespEnum>> monitorClasses = reflections.getSubTypesOf(AbstractLanguageRespEnum.class);
            for (Class<? extends AbstractLanguageRespEnum> m : monitorClasses) {
                try {
                    if("com.runlion.multilingual.enums.BizMultilingualStatusEnum".equals(m.getName())){
                        continue;
                    }
                    initEnumMessage(Class.forName(m.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initEnumMessage(Class enumClass){
        if(!EnumUtil.isEnum(enumClass)){
            return;
        }
        String enumClassSimpleName = enumClass.getSimpleName();
        // 初始化配置的外语
        List<String> languageCodeList = StrUtil.split(systemUsed, ',', true, true);

        for (String languageType : languageCodeList) {
            if(languageType.equals(LanguageEnum.ZH.getCode())){
                continue;
            }
            // 先查询数据库已有的数据，判断key是否存在saveOrUpdate
            QueryWrapper<Multilingual> multilingualQueryWrapper = new QueryWrapper<>();
            multilingualQueryWrapper.eq("word_target_type",languageType)
                    .likeRight("word_key",enumClassSimpleName);
            List<Multilingual> multilingualList = this.list(multilingualQueryWrapper);

            // 获取枚举类中的所有枚举
            List<Multilingual> saveList;
            LinkedHashMap<String, Enum> enumMap = EnumUtil.getEnumMap(enumClass);
            Set<Map.Entry<String, Enum>> enumEntries = enumMap.entrySet();
            ArrayList<Map.Entry<String, Enum>> entrieList = new ArrayList<>(enumEntries);

            saveList = transTask(entrieList, enumClassSimpleName, languageType, multilingualList);
            if(!CollectionUtils.isEmpty(saveList)){
                this.saveBatch(saveList);
            }
        }
    }


    private List<Multilingual> transTask(List<Map.Entry<String, Enum>> enumEntries, String enumClassSimpleName, String languageType, List<Multilingual> multilingualList) {
        List<Multilingual> saveList = new ArrayList<>();
        LanguageEnum languageEnum = LanguageEnum.getLanguageEnum(languageType);

        for (Map.Entry<String, Enum> enumEntry : enumEntries) {
            // 生成可以规则：枚举类名+“.”+枚举name
            String key = enumClassSimpleName + SEPARATOR + enumEntry.getKey();
            Field field = ReflectUtil.getField(enumEntry.getValue().getDeclaringClass(), "message");
            field.setAccessible(true);
            String message = "";
            try {
                message = (String) field.get(enumEntry.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 如果key已经存在，更新操作，不存在执行新增操作
            Multilingual multilingual = new Multilingual();
            multilingual.setWordKey(key)
                    .setWordSourceValue(message)
                    .setWordTargetType(languageType)
                    .setClientType(MultilingualClientTypeEnum.BACK_END_MSG.getCode());
            UpdateWrapper<Multilingual> multilingualUpdateWrapper = new UpdateWrapper<>();
            multilingualUpdateWrapper.eq("word_target_type", languageType)
                    .eq("word_key", key);
            long count = multilingualList.stream().filter(mul -> key.equals(mul.getWordKey())).count();
            if (count != 0) {
                update(multilingual, multilingualUpdateWrapper);
            } else {
                String wordTargetValue = "";
                // 自动翻译
                if (enableAutoTranslate && languageEnum.getBaiduEnable()) {
                    if(StrUtil.isBlank(appId) || StrUtil.isBlank(securityKey)){
                        throw new MultilingualException(BizMultilingualStatusEnum.BAIDU_TRANSLATE_ERROR);
                    }
                    TransApi baiduApi = new TransApi(appId, securityKey);
                    wordTargetValue = baiduApi.getTransResult(message, "auto", languageType);
                }
                multilingual.setWordTargetValue(wordTargetValue);
                saveList.add(multilingual);
            }
        }
        // 检查是否有漏掉未翻译的
        if(enableAutoTranslate && languageEnum.getBaiduEnable()){
            long count = saveList.stream().filter(multilingual -> StrUtil.isBlank(multilingual.getWordTargetValue())).count();
            while (count != 0){
                for (Multilingual multilingual : saveList) {
                    if(StrUtil.isBlank(multilingual.getWordTargetValue())){
                        TransApi baiduApi = new TransApi(appId, securityKey);
                        String value = baiduApi.getTransResult(multilingual.getWordSourceValue(), "auto", languageType);
                        multilingual.setWordTargetValue(value);
                        count--;
                    }
                }
            }
        }
        return saveList;


    }




    @Override
    public String getLanguageMessage(String enumClassName, String enumName,String message) {
        String key = enumClassName+SEPARATOR+enumName;
        // 当前请求语言类型
        String lang = LanguageInfoUtil.getCurrentLanguage();
        QueryWrapper<Multilingual> multilingualQueryWrapper = new QueryWrapper<>();
        if(!LanguageEnum.ZH.getCode().equals(lang)){
            multilingualQueryWrapper.eq("word_key",key).eq("word_target_type",lang).eq("deleted",false);
        }else{
            multilingualQueryWrapper.eq("word_key",key).eq("deleted",false);
        }
        List<Multilingual> multilingualList = this.list(multilingualQueryWrapper);
        // 如果数据库中没找到对应的目标语言，依然返回中文
        if(CollectionUtils.isEmpty(multilingualList)){
            return message;
        }
        String wordTargetValue;
        if(!LanguageEnum.ZH.getCode().equals(lang)){
            wordTargetValue = multilingualList.get(0).getWordTargetValue();
        }else {
            wordTargetValue = multilingualList.get(0).getWordSourceValue();
        }
        if(StringUtils.isEmpty(wordTargetValue)){
            return message;
        }
        return wordTargetValue;
    }


    @Override
    public <T extends Enum> T getMultilingualEnum(T eEnum) {
        Class enumClass = eEnum.getDeclaringClass();
        String enumClassName = enumClass.getSimpleName();
        String enumName = eEnum.name();
        String message = (String)ReflectUtil.getFieldValue(eEnum, "message");
        String languageMessage = this.getLanguageMessage(enumClassName, enumName, message);
        ReflectUtil.setFieldValue(eEnum,"message",languageMessage);
        return eEnum;
    }
}
