package com.runlion.multilingual.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 * @author wangyiting
 * @version 1.0 created 2018/8/28
 */
public class ReflectUtils {

    /**
     * 获取该类所有属性(包括父类)
     *
     * @author wangyiting created 2018/8/28
     * @param object 类对象
     * @return objects
     */
    public static Field[] getAllField(Object object) {
        //获取库位视图类的class
        Class clazz = object.getClass();
        //属性列表
        List<Field> fieldList = new ArrayList<>();
        //当class不为空时获得所有(包含父类)属性
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        //创建属性数组
        Field[] fields = new Field[fieldList.size()];
        //赋值
        fieldList.toArray(fields);
        return fields;
    }


}
