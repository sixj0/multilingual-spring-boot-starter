package com.runlion.multilingual.aspect;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runlion.multilingual.annotation.MultiLanguage;
import com.runlion.multilingual.annotation.MultiLanguageMethod;
import com.runlion.multilingual.dto.MultilingualDTO;
import com.runlion.multilingual.dto.MultilingualParamDto;
import com.runlion.multilingual.enums.MultilingualClientTypeEnum;
import com.runlion.multilingual.service.MultilingualService;
import com.runlion.multilingual.utils.ReflectUtils;
import com.runlion.multilingual.vo.MultilingualVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 多语言翻译切面
 *
 * @author wangyiting
 * @version 1.0 created 2019/1/24
 */
@Component
@Aspect
public class MultiLanguageAspect {

    @Autowired
    private MultilingualService multilingualService;

    @Around("@annotation(multiLanguageMethod)")
    public Object doAround(ProceedingJoinPoint joinPoint, MultiLanguageMethod multiLanguageMethod) throws Throwable {
        //获取method对象
        Object object;
        //获得返回结果
        object = joinPoint.proceed();
        //多语言客户端类型枚举
        MultilingualClientTypeEnum multilingualClientTypeEnum = multiLanguageMethod.multilingualClientTypeEnum();
        if (object instanceof IPage) {
            IPage page = (IPage) object;
            List list = page.getRecords();
            if (CollectionUtils.isEmpty(list)) {
                return object;
            }
            //转换数据
            this.conversionData(multilingualClientTypeEnum, list);
        }
        /// todo 动态获取数据
        /*else if (object instanceof LiteRestResponse){
            if (BaseRespStatusEnum.SUCCESS.getStatus() == ((LiteRestResponse) object).getStatus()) {
                Object data = ((LiteRestResponse) object).getData();
                if (!Objects.isNull(data)) {
                    //转换数据
                    this.conversionData(applicationEnum, multilingualClientTypeEnum, data);
                }
            }
        } */
        else {
            //转换数据
            this.conversionData(multilingualClientTypeEnum, object);
        }

        return object;
    }

    /**
     * 转换数据
     *
     * @author wangyiting created 2020/1/10
     * @param multilingualClientTypeEnum 多语言客户端类型枚举
     * @param object 数据对象
     */
    private void conversionData(MultilingualClientTypeEnum multilingualClientTypeEnum, Object object) {
        List list;
        //多语言数据
        List<MultilingualVO> multilingualVOS;
        //查询条件，参数为wordKey+"#"+wordSourceValue
        List<String> conditions = new ArrayList<>();
        if (object instanceof List) {
            list = (List) object;
            for (Object o : list) {
                //收集条件
                this.collectionConditions(conditions, o);
            }
            //没有需要翻译的数据
            if (CollectionUtils.isEmpty(conditions)) {
                return;
            }
            MultilingualDTO multilingualDTO = new MultilingualDTO();
            multilingualDTO.setMultilingualClientTypeEnum(multilingualClientTypeEnum).setConditions(conditions);
            //查询多语言数据
            multilingualVOS = multilingualService.listMultilingual(multilingualDTO);
            if (CollectionUtils.isEmpty(multilingualVOS)) {
                //初始化多语言数据
                this.initializeMultilingualData(multilingualClientTypeEnum, conditions);
                return;
            }
            for (Object o : list) {
                //设置多语言数据
                this.settingUpMultilingualData(multilingualVOS, o);
            }
        } else {
            //收集条件
            this.collectionConditions(conditions, object);
            //没有需要翻译的数据
            if (CollectionUtils.isEmpty(conditions)) {
                return;
            }
            MultilingualDTO multilingualDTO = new MultilingualDTO();
            multilingualDTO.setMultilingualClientTypeEnum(multilingualClientTypeEnum).setConditions(conditions);
            //查询多语言数据
            multilingualVOS = multilingualService.listMultilingual(multilingualDTO);
            if (CollectionUtils.isEmpty(multilingualVOS)) {
                //初始化多语言数据
                this.initializeMultilingualData(multilingualClientTypeEnum, conditions);
                return;
            }
            //设置多语言数据
            this.settingUpMultilingualData(multilingualVOS, object);
        }
        //保留没有生成过多语言数据的数据
        this.deleteAlreadyExists(conditions, multilingualVOS);
        if (!CollectionUtils.isEmpty(conditions)) {
            //初始化多语言数据
            this.initializeMultilingualData(multilingualClientTypeEnum, conditions);
        }
    }

    /**
     * 初始化多语言数据
     *
     * @author wangyiting created 2020/1/2
     * @param multilingualClientTypeEnum 多语言客户端类型
     * @param conditions 参数
     */
    private void initializeMultilingualData(MultilingualClientTypeEnum multilingualClientTypeEnum, List<String> conditions) {
        //去重
        List<String> collect = conditions.stream().distinct().collect(Collectors.toList());
        //根据wordKey分组
        Map<String, List<String>> collectMap = collect.parallelStream().collect(Collectors.groupingBy(info -> info.split("#")[0]));
        //提取wordSourceValue
        collectMap.forEach((key, value) -> {
            for (int i = 0; i < value.size(); i++) {
                value.set(i, value.get(i).split("#")[1]);
            }
        });
        MultilingualParamDto multilingualParamDto = new MultilingualParamDto();
        multilingualParamDto.setSourceMap(collectMap);
        multilingualParamDto.setMultilingualClientTypeEnum(multilingualClientTypeEnum);
        //插入多语言数据
        multilingualService.addMultilingualDb(multilingualParamDto);
    }

    /**
     * 清除已存在的多语言生成数据
     *
     * @author wangyiting created 2020/1/2
     * @param conditions 待生成的多语言数据
     * @param multilingualVOS 多语言数据查询结果
     */
    private void deleteAlreadyExists(List<String> conditions, List<MultilingualVO> multilingualVOS) {
        Iterator<String> iterator = conditions.iterator();
        //存在的多语言数据不需要重新设置
        while (iterator.hasNext()) {
            String next = iterator.next();
            for (MultilingualVO multilingualVO : multilingualVOS) {
                if (next.equals((multilingualVO.getWordKey() + "#" + multilingualVO.getWordSourceValue()))) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 设置多语言返回的目标语言数据
     *
     * @author wangyiting created 2020/1/2
     * @param multilingualVOS 多语言查询结果
     * @param o 目标object
     */
    private void settingUpMultilingualData(List<MultilingualVO> multilingualVOS, Object o) {
        Field[] fields = ReflectUtils.getAllField(o);
        Class classs = o.getClass();
        String voHeader;
        //是否存在该注解
        if (classs.isAnnotationPresent(MultiLanguage.class)) {
            MultiLanguage multiLanguage = (MultiLanguage) classs.getAnnotation(MultiLanguage.class);
            //获得注解配置的vo参数
            voHeader = multiLanguage.name();
            //对象名称
            String className = classs.getSimpleName();
            Arrays.stream(fields).forEach(field -> {
                //允许反射访问
                field.setAccessible(true);
                if (field.isAnnotationPresent(MultiLanguage.class)) {
                    try {
                        //设置数据
                        for (MultilingualVO multilingualVO : multilingualVOS) {
                            //对应上key且存在目标语言数据时设置
                            if ((multilingualVO.getWordKey() + "#" + multilingualVO.getWordSourceValue())
                                    .equals(StringUtils.isEmpty(voHeader) ? className : voHeader + "." + field.getName() + "#" + field.get(o))
                                    && !StringUtils.isEmpty(multilingualVO.getWordTargetValue())) {
                                field.set(o, multilingualVO.getWordTargetValue());
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 收集多语言查询条件
     *
     * @author wangyiting created 2020/1/2
     * @param conditions 查询条件
     * @param o vo对象
     */
    private void collectionConditions(List<String> conditions, Object o) {
        String voHeader;
        if (o == null) {
            return;
        }
        Field[] fields = ReflectUtils.getAllField(o);
        Class classs = o.getClass();
        if (classs.isAnnotationPresent(MultiLanguage.class)) {
            String className = classs.getSimpleName();
            MultiLanguage multiLanguage = (MultiLanguage) classs.getAnnotation(MultiLanguage.class);
            //获得注解配置的vo参数
            voHeader = multiLanguage.name();
            Arrays.stream(fields).forEach(field -> {
                //允许反射访问
                field.setAccessible(true);
                if (field.isAnnotationPresent(MultiLanguage.class)) {
                    try {
                        Object object = field.get(o);
                        if (object == null) {
                            return;
                        }
                        //过滤字符串存在""的情况
                        if (object instanceof String) {
                            if (!StringUtils.isEmpty(object)) {
                                //收集需要翻译的参数信息
                                conditions.add(StringUtils.isEmpty(voHeader) ? className : voHeader + "." + field.getName() + "#" + object);
                            }
                        } else {
                            //收集需要翻译的参数信息
                            conditions.add(StringUtils.isEmpty(voHeader) ? className : voHeader + "." + field.getName() + "#" + object);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
