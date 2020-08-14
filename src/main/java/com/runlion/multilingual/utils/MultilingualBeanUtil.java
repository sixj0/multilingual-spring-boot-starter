/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.runlion.multilingual.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sixiaojie
 * @date 2019-12-03 13:36
 */
public class MultilingualBeanUtil {
    public static <T,E> List<E> copyListProperties(List<T> sourceList, Class<E> clazz ){
        List<E> targetList = new ArrayList<>();
        for (T t : sourceList) {
            try{
                E e = clazz.newInstance();
                BeanUtils.copyProperties(t,e);
                targetList.add(e);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return targetList;
    }
}
