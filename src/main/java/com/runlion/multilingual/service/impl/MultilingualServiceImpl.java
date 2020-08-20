package com.runlion.multilingual.service.impl;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runlion.multilingual.annotation.MultiLanguageEnum;
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
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
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
import java.io.File;
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
    private static final String CLIENT_TYPE = "终端类型";
    private static final String KEY = "key";

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
    public List<Map<String, Object>> createExcelData(Integer clientType) {
        // 终端类型，key,中文(zh),英语(en),法语(fra),...
        List<Map<String, Object>> rows = new ArrayList<>();
        // 系统选用的外语
        List<String> codeList = StrUtil.split(systemUsed, ',', true, true);

        MultilingualClientTypeEnum clientEnum = MultilingualClientTypeEnum.getClientEnumByCode(clientType);
        if(Objects.isNull(clientEnum)){
            rows.add(createEmptyRow(codeList));
            return rows;
        }

        QueryWrapper<Multilingual> multilingualQueryWrapper = new QueryWrapper<>();
        multilingualQueryWrapper
                .eq("client_type",clientType)
                .eq("deleted",false);
        List<Multilingual> list = this.list(multilingualQueryWrapper);
        // 只有表头
        if(CollectionUtils.isEmpty(list)){
            rows.add(createEmptyRow(codeList));
            return rows;
        }
        Map<String, List<Multilingual>> listMap = list.stream().collect(Collectors.groupingBy(this::multilingualGruopBy));

        for (Map.Entry<String, List<Multilingual>> entry : listMap.entrySet()) {
            List<String> split = StrUtil.split(entry.getKey(), '#', true, true);
            String key = split.get(0);
            String zh = split.get(1);
            Map<String, Object> row = new LinkedHashMap<>();
            row.put(CLIENT_TYPE,clientEnum.getDesc());
            row.put(KEY,key);
            // 中文
            row.put(getLanguageHead(LanguageEnum.ZH.getCode()),zh);
            // 外语
            Map<String, String> wordTargetMap = entry.getValue().stream().collect(Collectors.toMap(Multilingual::getWordTargetType, Multilingual::getWordTargetValue));
            for (String code : codeList) {
                row.put(getLanguageHead(code),wordTargetMap.get(code));
            }
            rows.add(row);
        }

        return rows;
    }

    /**
     * 空表（只有表头）
     * @param codeList
     * @return
     */
    private Map<String, Object> createEmptyRow(List<String> codeList){
        Map<String, Object> row = new LinkedHashMap<>();
        row.put(CLIENT_TYPE,"");
        row.put(KEY,"");
        row.put(getLanguageHead(LanguageEnum.ZH.getCode()),"");
        for (String code : codeList) {
            row.put(getLanguageHead(code),"");
        }
        return row;
    }

    @Override
    public ExcelWriter createExcel(Integer clientType) {
        List<Map<String, Object>> rows = this.createExcelData(clientType);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 写数据
        writer.write(rows);
        // 设置样式
        writer.setColumnWidth(0,20);
        int columnCount = writer.getColumnCount(0);
        for (int i = 1; i < columnCount; i++) {
            writer.setColumnWidth(i,50);
        }
        // 左对齐
        StyleSet style = writer.getStyleSet();
        style.setAlign(HorizontalAlignment.LEFT,VerticalAlignment.CENTER);

        return writer;
    }


    private String getLanguageHead(String code){
        String desc = LanguageEnum.getDescByCode(code);
        return desc+"("+code+")";
    }

    /**
     * 分组依据
     * @param multilingual
     * @return
     */
    private String multilingualGruopBy(Multilingual multilingual){
        return multilingual.getWordKey()+"#"+multilingual.getWordSourceValue();
    }

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
        // 系统选用的外语
        List<String> codeList = StrUtil.split(systemUsed, ',', true, true);
        // 读数据
        List<Multilingual> multilingualList = new ArrayList<>();
        List<Map<String, Object>> rows = reader.readAll();
        for (Map<String, Object> row : rows) {
            String clientType = objectToStr(row.get(CLIENT_TYPE));
            MultilingualClientTypeEnum clientTypeEnum = MultilingualClientTypeEnum.getClientTypeEnum(clientType);
            if(Objects.isNull(clientTypeEnum)){
                throw new MultilingualException("上传失败：终端类型不正确，请检查后重新上传");
            }
            String key = objectToStr(row.get(KEY));
            if(StringUtils.isEmpty(key)){
                throw new MultilingualException("上传失败：key不能为空，请检查后重新上传");
            }
            String zh = objectToStr(row.get(getLanguageHead(LanguageEnum.ZH.getCode())));
            if(StringUtils.isEmpty(zh)){
                throw new MultilingualException("上传失败：中文不能为空，请检查后重新上传");
            }
            for (String code : codeList) {
                String targetValue = objectToStr(row.get(getLanguageHead(code)));
                Multilingual multilingual = new Multilingual(key, zh, targetValue, code, clientTypeEnum.getCode());
                multilingualList.add(multilingual);
            }
        }

        // 查询原有数据，根据key是否存在判断新增或者修改
        QueryWrapper<Multilingual> multilingualQueryWrapper = new QueryWrapper<>();
        multilingualQueryWrapper.eq("deleted",false);
        List<Multilingual> multilingualDbList = this.list(multilingualQueryWrapper);
        Map<String, Multilingual> keyMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(multilingualDbList)) {
            //将多语言数据组合成string用以对比
             keyMap = multilingualDbList.parallelStream().collect(Collectors.toMap(this::getString, multilingual -> multilingual));
        }

        // 新增或修改
        List<Multilingual> saveList = new ArrayList<>();
        for (Multilingual multilingual : multilingualList) {
            // 数据库中已经存在
            if(keyMap.containsKey(getString(multilingual))){
                Multilingual oldMultilingual = keyMap.get(getString(multilingual));
                if(multilingual.getWordTargetValue().equals(oldMultilingual.getWordTargetValue())){
                    continue;
                }
                // 修改
                UpdateWrapper<Multilingual> updateWrapper = new UpdateWrapper<>();
                updateWrapper
                        .eq("client_type",multilingual.getClientType())
                        .eq("word_target_type",multilingual.getWordTargetType())
                        .eq("word_key",multilingual.getWordKey())
                        .eq("word_source_value",multilingual.getWordSourceValue());
                this.update(multilingual,updateWrapper);
            }else{
                saveList.add(multilingual);
            }
        }

        if(!CollectionUtils.isEmpty(saveList)){
            this.saveBatch(saveList);
            // 自动百度翻译
            autoTranslate(saveList);
        }
    }

    /**
     * 自动翻译
     * @param list
     */
    private void autoTranslate(List<Multilingual> list){
        if(enableAutoTranslate){
            if(StrUtil.isBlank(appId) || StrUtil.isBlank(securityKey)){
                throw new MultilingualException(BizMultilingualStatusEnum.BAIDU_TRANSLATE_ERROR);
            }
            new Thread(()->{
                List<Multilingual> needTrans = list.stream().filter(multilingual -> LanguageEnum.getLanguageEnum(multilingual.getWordTargetType()).getBaiduEnable())
                        .collect(Collectors.toList());
                int count = needTrans.size();
                // 由于百度API的并发限制，可能会漏掉，所以使用while
                while (count != 0){
                    for (Multilingual multilingual : list) {
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
                if(!CollectionUtils.isEmpty(list)){
                    updateBatchById(list);
                }
            }).start();
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
    public List<String> downloadLangPackage(String clientType, String languageType) {
        MultilingualClientTypeEnum clientTypeEnum = MultilingualClientTypeEnum.getClientTypeEnum(clientType);
        if(Objects.isNull(clientTypeEnum)){
            return new ArrayList<>();
        }
        Integer clientCode = clientTypeEnum.getCode();

        QueryWrapper<Multilingual> multilingualQueryWrapper = new QueryWrapper<>();
        multilingualQueryWrapper.eq("word_target_type",languageType)
                .eq("client_type",clientCode)
                .eq("deleted",false);
        List<Multilingual> multilingualList = this.list(multilingualQueryWrapper);
        List<String> resultList = new ArrayList<>();
        // 前端:JSON格式文件
        if(MultilingualClientTypeEnum.FRONT.getCode().equals(clientCode)){
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
        if(MultilingualClientTypeEnum.APP_ANDROID.getCode().equals(clientCode)){
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
        if(MultilingualClientTypeEnum.APP_IOS.getCode().equals(clientCode)){
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
        autoTranslate(multilingualList);
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
                    .addScanners(new TypeAnnotationsScanner())
            );
            Set<Class<?>> typesAnnotated = reflections.getTypesAnnotatedWith(MultiLanguageEnum.class);
            for (Class<?> aClass : typesAnnotated) {
                try {
                    initEnumMessage(Class.forName(aClass.getName()));
                } catch (ClassNotFoundException e) {
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

            Enum anEnum = enumEntry.getValue();
            Class<? extends Enum> anEnumClass = anEnum.getClass();
            assert anEnumClass.isAnnotationPresent(MultiLanguageEnum.class);
            MultiLanguageEnum annotation = anEnumClass.getAnnotation(MultiLanguageEnum.class);
            Field field = ReflectUtil.getField(anEnum.getDeclaringClass(), annotation.fieldName());
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
        Class<? extends Enum> anEnumClass = eEnum.getClass();
        assert anEnumClass.isAnnotationPresent(MultiLanguageEnum.class);
        MultiLanguageEnum annotation = anEnumClass.getAnnotation(MultiLanguageEnum.class);
        String fieldName = annotation.fieldName();
        String message = (String)ReflectUtil.getFieldValue(eEnum, fieldName);
        String languageMessage = this.getLanguageMessage(enumClassName, enumName, message);
        ReflectUtil.setFieldValue(eEnum,fieldName,languageMessage);
        return eEnum;
    }
}
