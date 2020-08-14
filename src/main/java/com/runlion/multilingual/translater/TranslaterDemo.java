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
        // 英语
        String s1 = googleApi.translate("你好",  "en");
        // 印尼语
        String s2 = googleApi.translate("你好",  "id");
        // 德语
        String s3 = googleApi.translate("你好",  "de");
        // 法语
        String s4 = googleApi.translate("你好",  "fr");
        // 俄语
        String s5 = googleApi.translate("你好",  "ru");
        // 韩语
        String s6 = googleApi.translate("你好",  "ko");
        // 日语
        String s7 = googleApi.translate("你好",  "ja");
        // 泰语
        String s8 = googleApi.translate("你好",  "th");
        // 老挝语
        String s9 = googleApi.translate("你好",  "lo");


        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);
        System.out.println(s5);
        System.out.println(s6);
        System.out.println(s7);
        System.out.println(s8);
        System.out.println(s9);


    }
}
