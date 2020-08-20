/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.sixj.multilingual.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sixiaojie
 * @date 2019-12-15 10:22
 */
@Getter
@AllArgsConstructor
public enum LanguageEnum {

    AF("af","阿非利堪斯语",false),

    AM("am","阿姆哈拉语",false),

    AR("ara","阿拉伯语",true),

    AZ("az","阿塞拜疆语",false),

    BE("be","白俄罗斯语",false),

    BG("bul","保加利亚语",true),

    BN("bn","孟加拉语",false),

    BS("bs","波斯尼亚语",false),

    CA("ca","加泰隆语",false),

    CO("co","科西嘉语",false),

    CS("cs","捷克语",true),

    CY("cy","威尔士语",false),

    DA("dan","丹麦语",true),

    DE("de","德语",true),

    EL("el","希腊语",true),

    EN("en","英语",true),

    EO("eo","世界语",false),

    ES("spa","西班牙语",true),

    ET("est","爱沙尼亚语",true),

    EU("eu","巴斯克语",false),

    FA("fa","波斯语",false),

    FI("fin","芬兰语",true),

    FR("fra","法语",true),

    FY("fy","弗里西亚语",false),

    GA("ga","爱尔兰语",false),

    GD("gd","苏格兰盖尔语",false),

    GL("gl","加利西亚语",false),

    GU("gu","古吉拉特语",false),

    HA("ha","豪萨语",false),

    HI("hi","印地语",false),

    HR("hr","克罗地亚语",false),

    HT("ht","海地克里奥尔语",false),

    HU("hu","匈牙利语",true),

    HY("hy","亚美尼亚语",false),

    ID("id","印尼语",false),

    IG("ig","伊博语",false),

    IS("is","冰岛语",false),

    IT("it","意大利语",true),

    JA("jp","日语",true),

    JV("jv","爪哇语",false),

    KA("ka","格鲁吉亚语",false),

    KG("kg","刚果语",false),

    KK("kk","哈萨克语",false),

    KM("km","高棉语",false),

    KN("kn","卡纳达语",false),

    KO("kor","韩语",true),

    KU("ku","库尔德语",false),

    KY("ky","吉尔吉斯语",false),

    LA("la","拉丁语",false),

    LB("lb","卢森堡语",false),

    LO("lo","老挝语",false),

    LT("lt","立陶宛语",false),

    LV("lv","拉脱维亚语",false),

    MG("mg","马达加斯加语",false),

    MI("mi","毛利语",false),

    MK("mk","马其顿语",false),

    ML("ml","马拉亚拉姆语",false),

    MN("mn","蒙古语",false),

    MO("mo","摩尔达维亚语",false),

    MR("mr","马拉提语",false),

    MS("ms","马来语",false),

    MT("mt","马耳他语",false),

    MY("my","缅甸语",false),

    NB("nb","书面挪威语",false),

    NE("ne","尼泊尔语",false),

    NL("nl","荷兰语",true),

    NO("no","挪威语",false),

    NY("ny","尼扬贾语",false),

    OR("or","奥利亚语",false),

    PA("pa","旁遮普语",false),

    PL("pl","波兰语",true),

    PS("ps","普什图语",false),

    PT("pt","葡萄牙语",true),

    RO("rom","罗马尼亚语",true),

    RU("ru","俄语",true),

    RW("rw","基尼阿万达语",false),

    SD("sd","信德语",false),

    SH("sh","塞尔维亚-克罗地亚语",false),

    SI("si","僧加罗语",false),

    SK("sk","斯洛伐克语",false),

    SL("slo","斯洛文尼亚语",true),

    SM("sm","萨摩亚语",false),

    SN("sn","绍纳语",false),

    SO("so","索马里语",false),

    SQ("sq","阿尔巴尼亚语",false),

    SR("sr","塞尔维亚语",false),

    ST("st","南索托语",false),

    SU("su","巽他语",false),

    SV("swe","瑞典语",true),

    SW("sw","斯瓦希里语",false),

    TA("ta","泰米尔语",false),

    TE("te","泰卢固语",false),

    TG("tg","塔吉克语",false),

    TH("th","泰语",true),

    TK("tk","土库曼语",false),

    TL("tl","塔加洛语",false),

    TR("tr","土耳其语",false),

    TT("tt","塔塔尔语",false),

    UG("ug","维吾尔语",false),

    UK("uk","乌克兰语",false),

    UR("ur","乌尔都语",false),

    UZ("uz","乌兹别克语",false),

    VI("vie","越南语",true),

    XH("xh","科萨语",false),

    YI("yi","依地语",false),

    YO("yo","约鲁巴语",false),

    ZH("zh","中文",true),

    ZU("zu","祖鲁语",false),


    ;
    private String code;

    private String language;

    private Boolean baiduEnable;

    public static LanguageEnum getLanguageEnum(String code) {
        for (LanguageEnum state : LanguageEnum.values()) {
            if (state.code.equals(code)) {
                return state;
            }
        }
        return LanguageEnum.ZH;
    }

    public static String getDescByCode(String code){
        for (LanguageEnum languageEnum : LanguageEnum.values()) {
            if(languageEnum.getCode().equals(code)){
                return languageEnum.getLanguage();
            }
        }
        return "";
    }
    /**
     * 获取所有能被百度翻译的外语
     * @return
     */
    public static List<LanguageEnum> getBaiduEnableEnum(){
        ArrayList<LanguageEnum> result = new ArrayList<>();
        for (LanguageEnum languageEnum : LanguageEnum.values()) {
            if(languageEnum.baiduEnable){
                result.add(languageEnum);
            }
        }
        return result;
    }
}
