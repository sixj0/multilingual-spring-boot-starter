package com.runlion.multilingual.translater;

/**
 * @author sixiaojie
 * @date 2020-08-14-18:44
 */
public class TranslaterDemo {
    public static void main(String[] args) throws Exception {
        // 普通方式初始化
        GoogleApi googleApi = new GoogleApi();
        // 通过代理
//        GoogleApi googleApi = new GoogleApi("122.224.227.202", 3128);
        String result = googleApi.translate("hello world",  "zh");
        System.out.println(result);
    }
}
