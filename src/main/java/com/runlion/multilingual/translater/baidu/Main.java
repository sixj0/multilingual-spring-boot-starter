package com.runlion.multilingual.translater.baidu;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.runlion.multilingual.enums.LanguageEnum;

import java.util.*;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "";
    private static final String SECURITY_KEY = "";

    public static void main(String[] args) {
        /*TransApi api = new TransApi(APP_ID, SECURITY_KEY);

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
        }*/

        String s = "AF(\"af\",\"阿非利堪斯语\",false),\n" +
                "\n" +
                "    AM(\"am\",\"阿姆哈拉语\",false),\n" +
                "\n" +
                "    AR(\"ara\",\"阿拉伯语\",true),\n" +
                "\n" +
                "    AZ(\"az\",\"阿塞拜疆语\",false),\n" +
                "\n" +
                "    BE(\"be\",\"白俄罗斯语\",false),\n" +
                "\n" +
                "    BG(\"bul\",\"保加利亚语\",true),\n" +
                "\n" +
                "    BN(\"bn\",\"孟加拉语\",false),\n" +
                "\n" +
                "    BS(\"bs\",\"波斯尼亚语\",false),\n" +
                "\n" +
                "    CA(\"ca\",\"加泰隆语\",false),\n" +
                "\n" +
                "    CO(\"co\",\"科西嘉语\",false),\n" +
                "\n" +
                "    CS(\"cs\",\"捷克语\",true),\n" +
                "\n" +
                "    CY(\"cy\",\"威尔士语\",false),\n" +
                "\n" +
                "    DA(\"dan\",\"丹麦语\",true),\n" +
                "\n" +
                "    DE(\"de\",\"德语\",true),\n" +
                "\n" +
                "    EL(\"el\",\"希腊语\",true),\n" +
                "\n" +
                "    EN(\"en\",\"英语\",true),\n" +
                "\n" +
                "    EO(\"eo\",\"世界语\",false),\n" +
                "\n" +
                "    ES(\"spa\",\"西班牙语\",true),\n" +
                "\n" +
                "    ET(\"est\",\"爱沙尼亚语\",true),\n" +
                "\n" +
                "    EU(\"eu\",\"巴斯克语\",false),\n" +
                "\n" +
                "    FA(\"fa\",\"波斯语\",false),\n" +
                "\n" +
                "    FI(\"fin\",\"芬兰语\",true),\n" +
                "\n" +
                "    FR(\"fra\",\"法语\",true),\n" +
                "\n" +
                "    FY(\"fy\",\"弗里西亚语\",false),\n" +
                "\n" +
                "    GA(\"ga\",\"爱尔兰语\",false),\n" +
                "\n" +
                "    GD(\"gd\",\"苏格兰盖尔语\",false),\n" +
                "\n" +
                "    GL(\"gl\",\"加利西亚语\",false),\n" +
                "\n" +
                "    GU(\"gu\",\"古吉拉特语\",false),\n" +
                "\n" +
                "    HA(\"ha\",\"豪萨语\",false),\n" +
                "\n" +
                "    HI(\"hi\",\"印地语\",false),\n" +
                "\n" +
                "    HR(\"hr\",\"克罗地亚语\",false),\n" +
                "\n" +
                "    HT(\"ht\",\"海地克里奥尔语\",false),\n" +
                "\n" +
                "    HU(\"hu\",\"匈牙利语\",true),\n" +
                "\n" +
                "    HY(\"hy\",\"亚美尼亚语\",false),\n" +
                "\n" +
                "    ID(\"id\",\"印尼语\",false),\n" +
                "\n" +
                "    IG(\"ig\",\"伊博语\",false),\n" +
                "\n" +
                "    IS(\"is\",\"冰岛语\",false),\n" +
                "\n" +
                "    IT(\"it\",\"意大利语\",true),\n" +
                "\n" +
                "    JA(\"jp\",\"日语\",true),\n" +
                "\n" +
                "    JV(\"jv\",\"爪哇语\",false),\n" +
                "\n" +
                "    KA(\"ka\",\"格鲁吉亚语\",false),\n" +
                "\n" +
                "    KG(\"kg\",\"刚果语\",false),\n" +
                "\n" +
                "    KK(\"kk\",\"哈萨克语\",false),\n" +
                "\n" +
                "    KM(\"km\",\"高棉语\",false),\n" +
                "\n" +
                "    KN(\"kn\",\"卡纳达语\",false),\n" +
                "\n" +
                "    KO(\"kor\",\"韩语\",true),\n" +
                "\n" +
                "    KU(\"ku\",\"库尔德语\",false),\n" +
                "\n" +
                "    KY(\"ky\",\"吉尔吉斯语\",false),\n" +
                "\n" +
                "    LA(\"la\",\"拉丁语\",false),\n" +
                "\n" +
                "    LB(\"lb\",\"卢森堡语\",false),\n" +
                "\n" +
                "    LO(\"lo\",\"老挝语\",false),\n" +
                "\n" +
                "    LT(\"lt\",\"立陶宛语\",false),\n" +
                "\n" +
                "    LV(\"lv\",\"拉脱维亚语\",false),\n" +
                "\n" +
                "    MG(\"mg\",\"马达加斯加语\",false),\n" +
                "\n" +
                "    MI(\"mi\",\"毛利语\",false),\n" +
                "\n" +
                "    MK(\"mk\",\"马其顿语\",false),\n" +
                "\n" +
                "    ML(\"ml\",\"马拉亚拉姆语\",false),\n" +
                "\n" +
                "    MN(\"mn\",\"蒙古语\",false),\n" +
                "\n" +
                "    MO(\"mo\",\"摩尔达维亚语\",false),\n" +
                "\n" +
                "    MR(\"mr\",\"马拉提语\",false),\n" +
                "\n" +
                "    MS(\"ms\",\"马来语\",false),\n" +
                "\n" +
                "    MT(\"mt\",\"马耳他语\",false),\n" +
                "\n" +
                "    MY(\"my\",\"缅甸语\",false),\n" +
                "\n" +
                "    NB(\"nb\",\"书面挪威语\",false),\n" +
                "\n" +
                "    NE(\"ne\",\"尼泊尔语\",false),\n" +
                "\n" +
                "    NL(\"nl\",\"荷兰语\",true),\n" +
                "\n" +
                "    NO(\"no\",\"挪威语\",false),\n" +
                "\n" +
                "    NY(\"ny\",\"尼扬贾语\",false),\n" +
                "\n" +
                "    OR(\"or\",\"奥利亚语\",false),\n" +
                "\n" +
                "    PA(\"pa\",\"旁遮普语\",false),\n" +
                "\n" +
                "    PL(\"pl\",\"波兰语\",true),\n" +
                "\n" +
                "    PS(\"ps\",\"普什图语\",false),\n" +
                "\n" +
                "    PT(\"pt\",\"葡萄牙语\",true),\n" +
                "\n" +
                "    RO(\"rom\",\"罗马尼亚语\",true),\n" +
                "\n" +
                "    RU(\"ru\",\"俄语\",true),\n" +
                "\n" +
                "    RW(\"rw\",\"基尼阿万达语\",false),\n" +
                "\n" +
                "    SD(\"sd\",\"信德语\",false),\n" +
                "\n" +
                "    SH(\"sh\",\"塞尔维亚-克罗地亚语\",false),\n" +
                "\n" +
                "    SI(\"si\",\"僧加罗语\",false),\n" +
                "\n" +
                "    SK(\"sk\",\"斯洛伐克语\",false),\n" +
                "\n" +
                "    SL(\"slo\",\"斯洛文尼亚语\",true),\n" +
                "\n" +
                "    SM(\"sm\",\"萨摩亚语\",false),\n" +
                "\n" +
                "    SN(\"sn\",\"绍纳语\",false),\n" +
                "\n" +
                "    SO(\"so\",\"索马里语\",false),\n" +
                "\n" +
                "    SQ(\"sq\",\"阿尔巴尼亚语\",false),\n" +
                "\n" +
                "    SR(\"sr\",\"塞尔维亚语\",false),\n" +
                "\n" +
                "    ST(\"st\",\"南索托语\",false),\n" +
                "\n" +
                "    SU(\"su\",\"巽他语\",false),\n" +
                "\n" +
                "    SV(\"swe\",\"瑞典语\",true),\n" +
                "\n" +
                "    SW(\"sw\",\"斯瓦希里语\",false),\n" +
                "\n" +
                "    TA(\"ta\",\"泰米尔语\",false),\n" +
                "\n" +
                "    TE(\"te\",\"泰卢固语\",false),\n" +
                "\n" +
                "    TG(\"tg\",\"塔吉克语\",false),\n" +
                "\n" +
                "    TH(\"th\",\"泰语\",true),\n" +
                "\n" +
                "    TK(\"tk\",\"土库曼语\",false),\n" +
                "\n" +
                "    TL(\"tl\",\"塔加洛语\",false),\n" +
                "\n" +
                "    TR(\"tr\",\"土耳其语\",false),\n" +
                "\n" +
                "    TT(\"tt\",\"塔塔尔语\",false),\n" +
                "\n" +
                "    UG(\"ug\",\"维吾尔语\",false),\n" +
                "\n" +
                "    UK(\"uk\",\"乌克兰语\",false),\n" +
                "\n" +
                "    UR(\"ur\",\"乌尔都语\",false),\n" +
                "\n" +
                "    UZ(\"uz\",\"乌兹别克语\",false),\n" +
                "\n" +
                "    VI(\"vie\",\"越南语\",true),\n" +
                "\n" +
                "    XH(\"xh\",\"科萨语\",false),\n" +
                "\n" +
                "    YI(\"yi\",\"依地语\",false),\n" +
                "\n" +
                "    YO(\"yo\",\"约鲁巴语\",false),\n" +
                "\n" +
                "    ZH(\"zh\",\"中文\",true),\n" +
                "\n" +
                "    ZU(\"zu\",\"祖鲁语\",false),";

//        s = s.replace("\"", "");
//        s = s.replace("(","");
//        s = s.replace(")","");
        ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        List<String> split = StrUtil.split(s, '\n', true, true);
        for (String s1 : split) {
            String substring = s1.substring(3, s1.length()-2);
            substring = substring.replace("\"","");
            System.out.println(substring);
            Map<String, Object> row = new LinkedHashMap<>();
            String[] split1 = substring.split(",");
            row.put("编码",split1[0]);
            row.put("语言",split1[1]);
            row.put("是否支持百度翻译",split1[2].equals("true") ? 1:0);
            rows.add(row);
        }
        ExcelWriter writer = ExcelUtil.getWriter("/Users/sixj/Desktop/language.xlsx");
        writer.write(rows);

        writer.close();


    }

}
