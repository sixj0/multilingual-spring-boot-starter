package com.runlion.multilingual.translater.baidu;


import com.runlion.multilingual.enums.LanguageEnum;

import java.util.List;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20200817000544924";
    private static final String SECURITY_KEY = "XouCPfoiDQxJt0pcCloW";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "高度600米";

        List<LanguageEnum> enableBaidu = LanguageEnum.getBaiduEnableEnum();
        for (LanguageEnum languageEnum : enableBaidu) {
            String code = languageEnum.getCode();
            String language = languageEnum.getLanguage();
            try {
                System.out.println(language+":"+api.getTransResult(query, "auto", code));
            }catch (Exception e){
                System.err.println(language);
            }
        }

    }

}
