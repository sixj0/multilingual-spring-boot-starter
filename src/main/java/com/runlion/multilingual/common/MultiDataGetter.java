package com.runlion.multilingual.common;

/**
 * @author sixiaojie
 * @date 2020-08-19-13:35
 */
public interface MultiDataGetter<T> {

    /**
     * 对于复杂的返回值类型，需要实现该方法，返回需要转换多语言的对象，或者对象集合
     * @param o
     * @return
     */
    public Object getData(T o);
}
