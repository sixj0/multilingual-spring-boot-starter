package com.runlion.multilingual.translater;

import cn.hutool.core.util.StrUtil;
import org.apache.tomcat.jni.Local;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sixiaojie
 * @date 2020-08-14-18:44
 */
public class TranslaterDemo {
    public static void main(String[] args) throws Exception {
//        GoogleApi googleApi = new GoogleApi();
//        String[] isoLanguages = Locale.getISOLanguages();
//        HashMap<String, String> map = new HashMap<>();
//        for (String isoLanguage : isoLanguages) {
//            String translate = googleApi.translate("你好", isoLanguage);
//            System.out.println(isoLanguage+","+translate);
//            map.put(isoLanguage,translate);
//        }
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            System.out.println(entry.getKey()+"-->"+entry.getValue());
//        }


        // 普通方式初始化
//        GoogleApi googleApi = new GoogleApi();
//        // 通过代理
////        GoogleApi googleApi = new GoogleApi("122.224.227.202", 3128);
//        // 英语
//        String s1 = googleApi.translate("你好",  "en");
//        // 印尼语
//        String s2 = googleApi.translate("你好",  "id");
//        // 德语
//        String s3 = googleApi.translate("你好",  "de");
//        // 法语
//        String s4 = googleApi.translate("你好",  "fr");
//        // 俄语
//        String s5 = googleApi.translate("你好",  "ru");
//        // 韩语
//        String s6 = googleApi.translate("你好",  "ko");
//        // 日语
//        String s7 = googleApi.translate("你好",  "ja");
//        // 泰语
//        String s8 = googleApi.translate("你好",  "th");
//        // 老挝语
//        String s9 = googleApi.translate("你好",  "lo");
//        String s10 = googleApi.translate("你好，晓",  "zh-TW");
//
//
//        System.out.println(s1);
//        System.out.println(s2);
//        System.out.println(s3);
//        System.out.println(s4);
//        System.out.println(s5);
//        System.out.println(s6);
//        System.out.println(s7);
//        System.out.println(s8);
//        System.out.println(s9);
//        System.out.println(s10);



//        String s1 = "aa      阿法尔语        fr      法语            li      林堡语          se      北萨米语\n" +
//                "ab      阿布哈兹语      fy      弗里西亚语      ln      林加拉语        sg      桑戈语\n" +
//                "ae      阿维斯陀语      ga      爱尔兰语        lo      老挝语          sh      塞尔维亚-克罗地亚语\n" +
//                "af      阿非利堪斯语    gd      苏格兰盖尔语    lt      立陶宛语        si      僧加罗语\n" +
//                "ak      阿坎语          gl      加利西亚语      lu      卢巴-加丹加语   sk      斯洛伐克语\n" +
//                "am      阿姆哈拉语      gn      瓜拉尼语        lv      拉脱维亚语      sl      斯洛文尼亚语\n" +
//                "an      阿拉贡语        gu      古吉拉特语      mg      马达加斯加语    sm      萨摩亚语\n" +
//                "ar      阿拉伯语        gv      马恩岛语        mh      马绍尔语        sn      绍纳语\n" +
//                "as      阿萨姆语        ha      豪萨语          mi      毛利语          so      索马里语\n" +
//                "av      阿瓦尔语        he      希伯来语        mk      马其顿语        sq      阿尔巴尼亚语\n" +
//                "ay      艾马拉语        hi      印地语          ml      马拉亚拉姆语    sr      塞尔维亚语\n" +
//                "az      阿塞拜疆语      ho      希里莫图语      mn      蒙古语          ss      斯瓦特语\n" +
//                "ba      巴什基尔语      hr      克罗地亚语      mo      摩尔达维亚语    st      南索托语\n" +
//                "be      白俄罗斯语      ht      海地克里奥尔语  mr      马拉提语        su      巽他语\n" +
//                "bg      保加利亚语      hu      匈牙利语        ms      马来语          sv      瑞典语\n" +
//                "bh      比哈尔语        hy      亚美尼亚语      mt      马耳他语        sw      斯瓦希里语\n" +
//                "bi      比斯拉玛语      hz      赫雷罗语        my      缅甸语          ta      泰米尔语\n" +
//                "bm      班巴拉语        ia      国际语A         na      瑙鲁语          te      泰卢固语\n" +
//                "bn      孟加拉语        id      印尼语          nb      书面挪威语      tg      塔吉克语\n" +
//                "bo      藏语            ie      国际语E         nd      北恩德贝勒语    th      泰语\n" +
//                "br      布列塔尼语      ig      伊博语          ne      尼泊尔语        ti      提格里尼亚语\n" +
//                "bs      波斯尼亚语      ii      四川彝语        ng      恩敦加语        tk      土库曼语\n" +
//                "ca      加泰隆语        ik      依努庇克语      nl      荷兰语          tl      塔加洛语\n" +
//                "ce      车臣语          io      伊多语          nn      新挪威语        tn      塞茨瓦纳语\n" +
//                "ch      查莫罗语        is      冰岛语          no      挪威语          to      汤加语\n" +
//                "co      科西嘉语        it      意大利语        nr      南恩德贝勒语    tr      土耳其语\n" +
//                "cr      克里语          iu      伊努伊特语      nv      纳瓦霍语        ts      宗加语\n" +
//                "cs      捷克语          ja      日语            ny      尼扬贾语        tt      塔塔尔语\n" +
//                "cu      教会斯拉夫语    jv      爪哇语          oc      奥克语          tw      特威语\n" +
//                "cv      楚瓦什语        ka      格鲁吉亚语      oj      奥吉布瓦语      ty      塔希提语\n" +
//                "cy      威尔士语        kg      刚果语          om      奥洛莫语        ug      维吾尔语\n" +
//                "da      丹麦语          ki      基库尤语        or      奥利亚语        uk      乌克兰语\n" +
//                "de      德语            kj      宽亚玛语        os      奥塞梯语        ur      乌尔都语\n" +
//                "dv      迪维希语        kk      哈萨克语        pa      旁遮普语        uz      乌兹别克语\n" +
//                "dz      不丹语          kl      格陵兰语        pi      巴利语          ve      文达语\n" +
//                "ee      埃维语          km      高棉语          pl      波兰语          vi      越南语\n" +
//                "el      现代希腊语      kn      卡纳达语        ps      普什图语        vo      沃拉普克语\n" +
//                "en      英语            ko      朝鲜语、韩语    pt      葡萄牙语        wa      沃伦语\n" +
//                "eo      世界语          kr      卡努里语        qu      凯楚亚语        wo      沃洛夫语\n" +
//                "es      西班牙语        ks      克什米尔语      rm      利托-罗曼语     xh      科萨语\n" +
//                "et      爱沙尼亚语      ku      库尔德语        rn      基隆迪语        yi      依地语\n" +
//                "eu      巴斯克语        kv      科米语          ro      罗马尼亚语      yo      约鲁巴语\n" +
//                "fa      波斯语          kw      康沃尔语        ru      俄语            za      壮语\n" +
//                "ff      富拉语          ky      吉尔吉斯语      rw      基尼阿万达语    zh      中文、汉语\n" +
//                "fi      芬兰语          la      拉丁语          sa      梵语            zu      祖鲁语\n" +
//                "fj      斐济语          lb      卢森堡语        sc      撒丁语          \n" +
//                "fo      法罗语          lg      干达语          sd      信德语  \n";
//        List<String> split = StrUtil.split(s1, ' ', true, true);
//        split.forEach(System.out::println);
//
//        String s2 = "阿尔巴尼亚语\n" +
//                "阿拉伯语\n" +
//                "阿姆哈拉语\n" +
//                "阿塞拜疆语\n" +
//                "爱尔兰语\n" +
//                "爱沙尼亚语\n" +
//                "奥里亚语(奥里亚文)\n" +
//                "巴斯克语\n" +
//                "白俄罗斯语\n" +
//                "保加利亚语\n" +
//                "冰岛语\n" +
//                "波兰语\n" +
//                "波斯尼亚语\n" +
//                "波斯语\n" +
//                "布尔语(南非荷兰语)\n" +
//                "鞑靼语\n" +
//                "丹麦语\n" +
//                "德语\n" +
//                "俄语\n" +
//                "法语\n" +
//                "菲律宾语\n" +
//                "芬兰语\n" +
//                "弗里西语\n" +
//                "高棉语\n" +
//                "格鲁吉亚语\n" +
//                "古吉拉特语\n" +
//                "哈萨克语\n" +
//                "海地克里奥尔语\n" +
//                "韩语\n" +
//                "豪萨语\n" +
//                "荷兰语\n" +
//                "吉尔吉斯语\n" +
//                "加利西亚语\n" +
//                "加泰罗尼亚语\n" +
//                "捷克语\n" +
//                "卡纳达语\n" +
//                "科西嘉语\n" +
//                "克罗地亚语\n" +
//                "库尔德语\n" +
//                "拉丁语\n" +
//                "拉脱维亚语\n" +
//                "老挝语\n" +
//                "立陶宛语\n" +
//                "卢森堡语\n" +
//                "卢旺达语\n" +
//                "罗马尼亚语\n" +
//                "马尔加什语\n" +
//                "马耳他语\n" +
//                "马拉地语\n" +
//                "马拉雅拉姆语\n" +
//                "马来语\n" +
//                "马其顿语\n" +
//                "毛利语\n" +
//                "蒙古语\n" +
//                "孟加拉语\n" +
//                "缅甸语\n" +
//                "苗语\n" +
//                "南非科萨语\n" +
//                "南非祖鲁语\n" +
//                "尼泊尔语\n" +
//                "挪威语\n" +
//                "旁遮普语\n" +
//                "葡萄牙语\n" +
//                "普什图语\n" +
//                "齐切瓦语\n" +
//                "日语\n" +
//                "瑞典语\n" +
//                "萨摩亚语\n" +
//                "塞尔维亚语\n" +
//                "塞索托语\n" +
//                "僧伽罗语\n" +
//                "世界语\n" +
//                "斯洛伐克语\n" +
//                "斯洛文尼亚语\n" +
//                "斯瓦希里语\n" +
//                "苏格兰盖尔语\n" +
//                "宿务语\n" +
//                "索马里语\n" +
//                "塔吉克语\n" +
//                "泰卢固语\n" +
//                "泰米尔语\n" +
//                "泰语\n" +
//                "土耳其语\n" +
//                "土库曼语\n" +
//                "威尔士语\n" +
//                "维吾尔语\n" +
//                "乌尔都语\n" +
//                "乌克兰语\n" +
//                "乌兹别克语\n" +
//                "西班牙语\n" +
//                "希伯来语\n" +
//                "希腊语\n" +
//                "夏威夷语\n" +
//                "信德语\n" +
//                "匈牙利语\n" +
//                "修纳语\n" +
//                "亚美尼亚语\n" +
//                "伊博语\n" +
//                "意大利语\n" +
//                "意第绪语\n" +
//                "印地语\n" +
//                "印尼巽他语\n" +
//                "印尼语\n" +
//                "印尼爪哇语\n" +
//                "英语\n" +
//                "约鲁巴语\n" +
//                "越南语\n" +
//                "中文(繁体)\n" +
//                "中文(简体)";
//        List<String> google = StrUtil.split(s2, '\n', true, true);
////        for (String s : google) {
////            System.out.println("/** "+s+" */");
////        }
//
//
//        String s3 = "aa\n" +
//                "阿法尔语\n" +
//                "fr\n" +
//                "法语\n" +
//                "li\n" +
//                "林堡语\n" +
//                "se\n" +
//                "北萨米语\n" +
//                "ab\n" +
//                "阿布哈兹语\n" +
//                "fy\n" +
//                "弗里西亚语\n" +
//                "ln\n" +
//                "林加拉语\n" +
//                "sg\n" +
//                "桑戈语\n" +
//                "ae\n" +
//                "阿维斯陀语\n" +
//                "ga\n" +
//                "爱尔兰语\n" +
//                "lo\n" +
//                "老挝语\n" +
//                "sh\n" +
//                "塞尔维亚-克罗地亚语\n" +
//                "af\n" +
//                "阿非利堪斯语\n" +
//                "gd\n" +
//                "苏格兰盖尔语\n" +
//                "lt\n" +
//                "立陶宛语\n" +
//                "si\n" +
//                "僧加罗语\n" +
//                "ak\n" +
//                "阿坎语\n" +
//                "gl\n" +
//                "加利西亚语\n" +
//                "lu\n" +
//                "卢巴-加丹加语\n" +
//                "sk\n" +
//                "斯洛伐克语\n" +
//                "am\n" +
//                "阿姆哈拉语\n" +
//                "gn\n" +
//                "瓜拉尼语\n" +
//                "lv\n" +
//                "拉脱维亚语\n" +
//                "sl\n" +
//                "斯洛文尼亚语\n" +
//                "an\n" +
//                "阿拉贡语\n" +
//                "gu\n" +
//                "古吉拉特语\n" +
//                "mg\n" +
//                "马达加斯加语\n" +
//                "sm\n" +
//                "萨摩亚语\n" +
//                "ar\n" +
//                "阿拉伯语\n" +
//                "gv\n" +
//                "马恩岛语\n" +
//                "mh\n" +
//                "马绍尔语\n" +
//                "sn\n" +
//                "绍纳语\n" +
//                "as\n" +
//                "阿萨姆语\n" +
//                "ha\n" +
//                "豪萨语\n" +
//                "mi\n" +
//                "毛利语\n" +
//                "so\n" +
//                "索马里语\n" +
//                "av\n" +
//                "阿瓦尔语\n" +
//                "he\n" +
//                "希伯来语\n" +
//                "mk\n" +
//                "马其顿语\n" +
//                "sq\n" +
//                "阿尔巴尼亚语\n" +
//                "ay\n" +
//                "艾马拉语\n" +
//                "hi\n" +
//                "印地语\n" +
//                "ml\n" +
//                "马拉亚拉姆语\n" +
//                "sr\n" +
//                "塞尔维亚语\n" +
//                "az\n" +
//                "阿塞拜疆语\n" +
//                "ho\n" +
//                "希里莫图语\n" +
//                "mn\n" +
//                "蒙古语\n" +
//                "ss\n" +
//                "斯瓦特语\n" +
//                "ba\n" +
//                "巴什基尔语\n" +
//                "hr\n" +
//                "克罗地亚语\n" +
//                "mo\n" +
//                "摩尔达维亚语\n" +
//                "st\n" +
//                "南索托语\n" +
//                "be\n" +
//                "白俄罗斯语\n" +
//                "ht\n" +
//                "海地克里奥尔语\n" +
//                "mr\n" +
//                "马拉提语\n" +
//                "su\n" +
//                "巽他语\n" +
//                "bg\n" +
//                "保加利亚语\n" +
//                "hu\n" +
//                "匈牙利语\n" +
//                "ms\n" +
//                "马来语\n" +
//                "sv\n" +
//                "瑞典语\n" +
//                "bh\n" +
//                "比哈尔语\n" +
//                "hy\n" +
//                "亚美尼亚语\n" +
//                "mt\n" +
//                "马耳他语\n" +
//                "sw\n" +
//                "斯瓦希里语\n" +
//                "bi\n" +
//                "比斯拉玛语\n" +
//                "hz\n" +
//                "赫雷罗语\n" +
//                "my\n" +
//                "缅甸语\n" +
//                "ta\n" +
//                "泰米尔语\n" +
//                "bm\n" +
//                "班巴拉语\n" +
//                "ia\n" +
//                "国际语A\n" +
//                "na\n" +
//                "瑙鲁语\n" +
//                "te\n" +
//                "泰卢固语\n" +
//                "bn\n" +
//                "孟加拉语\n" +
//                "id\n" +
//                "印尼语\n" +
//                "nb\n" +
//                "书面挪威语\n" +
//                "tg\n" +
//                "塔吉克语\n" +
//                "bo\n" +
//                "藏语\n" +
//                "ie\n" +
//                "国际语E\n" +
//                "nd\n" +
//                "北恩德贝勒语\n" +
//                "th\n" +
//                "泰语\n" +
//                "br\n" +
//                "布列塔尼语\n" +
//                "ig\n" +
//                "伊博语\n" +
//                "ne\n" +
//                "尼泊尔语\n" +
//                "ti\n" +
//                "提格里尼亚语\n" +
//                "bs\n" +
//                "波斯尼亚语\n" +
//                "ii\n" +
//                "四川彝语\n" +
//                "ng\n" +
//                "恩敦加语\n" +
//                "tk\n" +
//                "土库曼语\n" +
//                "ca\n" +
//                "加泰隆语\n" +
//                "ik\n" +
//                "依努庇克语\n" +
//                "nl\n" +
//                "荷兰语\n" +
//                "tl\n" +
//                "塔加洛语\n" +
//                "ce\n" +
//                "车臣语\n" +
//                "io\n" +
//                "伊多语\n" +
//                "nn\n" +
//                "新挪威语\n" +
//                "tn\n" +
//                "塞茨瓦纳语\n" +
//                "ch\n" +
//                "查莫罗语\n" +
//                "is\n" +
//                "冰岛语\n" +
//                "no\n" +
//                "挪威语\n" +
//                "to\n" +
//                "汤加语\n" +
//                "co\n" +
//                "科西嘉语\n" +
//                "it\n" +
//                "意大利语\n" +
//                "nr\n" +
//                "南恩德贝勒语\n" +
//                "tr\n" +
//                "土耳其语\n" +
//                "cr\n" +
//                "克里语\n" +
//                "iu\n" +
//                "伊努伊特语\n" +
//                "nv\n" +
//                "纳瓦霍语\n" +
//                "ts\n" +
//                "宗加语\n" +
//                "cs\n" +
//                "捷克语\n" +
//                "ja\n" +
//                "日语\n" +
//                "ny\n" +
//                "尼扬贾语\n" +
//                "tt\n" +
//                "塔塔尔语\n" +
//                "cu\n" +
//                "教会斯拉夫语\n" +
//                "jv\n" +
//                "爪哇语\n" +
//                "oc\n" +
//                "奥克语\n" +
//                "tw\n" +
//                "特威语\n" +
//                "cv\n" +
//                "楚瓦什语\n" +
//                "ka\n" +
//                "格鲁吉亚语\n" +
//                "oj\n" +
//                "奥吉布瓦语\n" +
//                "ty\n" +
//                "塔希提语\n" +
//                "cy\n" +
//                "威尔士语\n" +
//                "kg\n" +
//                "刚果语\n" +
//                "om\n" +
//                "奥洛莫语\n" +
//                "ug\n" +
//                "维吾尔语\n" +
//                "da\n" +
//                "丹麦语\n" +
//                "ki\n" +
//                "基库尤语\n" +
//                "or\n" +
//                "奥利亚语\n" +
//                "uk\n" +
//                "乌克兰语\n" +
//                "de\n" +
//                "德语\n" +
//                "kj\n" +
//                "宽亚玛语\n" +
//                "os\n" +
//                "奥塞梯语\n" +
//                "ur\n" +
//                "乌尔都语\n" +
//                "dv\n" +
//                "迪维希语\n" +
//                "kk\n" +
//                "哈萨克语\n" +
//                "pa\n" +
//                "旁遮普语\n" +
//                "uz\n" +
//                "乌兹别克语\n" +
//                "dz\n" +
//                "不丹语\n" +
//                "kl\n" +
//                "格陵兰语\n" +
//                "pi\n" +
//                "巴利语\n" +
//                "ve\n" +
//                "文达语\n" +
//                "ee\n" +
//                "埃维语\n" +
//                "km\n" +
//                "高棉语\n" +
//                "pl\n" +
//                "波兰语\n" +
//                "vi\n" +
//                "越南语\n" +
//                "el\n" +
//                "现代希腊语\n" +
//                "kn\n" +
//                "卡纳达语\n" +
//                "ps\n" +
//                "普什图语\n" +
//                "vo\n" +
//                "沃拉普克语\n" +
//                "en\n" +
//                "英语\n" +
//                "ko\n" +
//                "朝鲜语、韩语\n" +
//                "pt\n" +
//                "葡萄牙语\n" +
//                "wa\n" +
//                "沃伦语\n" +
//                "eo\n" +
//                "世界语\n" +
//                "kr\n" +
//                "卡努里语\n" +
//                "qu\n" +
//                "凯楚亚语\n" +
//                "wo\n" +
//                "沃洛夫语\n" +
//                "es\n" +
//                "西班牙语\n" +
//                "ks\n" +
//                "克什米尔语\n" +
//                "rm\n" +
//                "利托-罗曼语\n" +
//                "xh\n" +
//                "科萨语\n" +
//                "et\n" +
//                "爱沙尼亚语\n" +
//                "ku\n" +
//                "库尔德语\n" +
//                "rn\n" +
//                "基隆迪语\n" +
//                "yi\n" +
//                "依地语\n" +
//                "eu\n" +
//                "巴斯克语\n" +
//                "kv\n" +
//                "科米语\n" +
//                "ro\n" +
//                "罗马尼亚语\n" +
//                "yo\n" +
//                "约鲁巴语\n" +
//                "fa\n" +
//                "波斯语\n" +
//                "kw\n" +
//                "康沃尔语\n" +
//                "ru\n" +
//                "俄语\n" +
//                "za\n" +
//                "壮语\n" +
//                "ff\n" +
//                "富拉语\n" +
//                "ky\n" +
//                "吉尔吉斯语\n" +
//                "rw\n" +
//                "基尼阿万达语\n" +
//                "zh\n" +
//                "中文、汉语\n" +
//                "fi\n" +
//                "芬兰语\n" +
//                "la\n" +
//                "拉丁语\n" +
//                "sa\n" +
//                "梵语\n" +
//                "zu\n" +
//                "祖鲁语\n" +
//                "fj\n" +
//                "斐济语\n" +
//                "lb\n" +
//                "卢森堡语\n" +
//                "sc\n" +
//                "撒丁语\n" +
//                "fo\n" +
//                "法罗语\n" +
//                "lg\n" +
//                "干达语\n" +
//                "sd\n" +
//                "信德语";
//
//        List<String> all = StrUtil.split(s3, '\n', true, true);
//
//
//        HashMap<String, String> languageMap = new LinkedHashMap<>();
//
//        for (int i = 1; i < all.size(); i+=2) {
//            languageMap.put(all.get(i),all.get(i-1));
//        }
//
//        for (Map.Entry<String, String> entry : languageMap.entrySet()) {
//            System.out.println(entry);
//        }
//
//        Map<String, String> enableGoogle = new ConcurrentHashMap<>();
//        GoogleApi googleApi = new GoogleApi();
//        languageMap.entrySet().parallelStream().forEach(entry->{
//            try {
//                googleApi.translate("你好",entry.getValue());
//                enableGoogle.put(entry.getKey(),entry.getValue());
//            }catch (Exception e){
//                System.err.println(entry.getKey());
//            }
//        });
//        System.out.println("===================");
//        Set<Map.Entry<String, String>> entries = enableGoogle.entrySet();
//        entries.forEach(entry -> {
//            System.out.println("/** "+entry.getKey()+" */");
//            String value = entry.getValue();
//            System.out.println(value.toUpperCase()+"(\""+value+"\"),");
//        });
//
//        System.out.println(enableGoogle.size());

        String s = "/** 乌兹别克语 */\n" +
                "UZ(\"uz\"),\n" +
                "/** 保加利亚语 */\n" +
                "BG(\"bg\"),\n" +
                "/** 萨摩亚语 */\n" +
                "SM(\"sm\"),\n" +
                "/** 爪哇语 */\n" +
                "JV(\"jv\"),\n" +
                "/** 马拉提语 */\n" +
                "MR(\"mr\"),\n" +
                "/** 英语 */\n" +
                "EN(\"en\"),\n" +
                "/** 塞尔维亚-克罗地亚语 */\n" +
                "SH(\"sh\"),\n" +
                "/** 阿姆哈拉语 */\n" +
                "AM(\"am\"),\n" +
                "/** 克罗地亚语 */\n" +
                "HR(\"hr\"),\n" +
                "/** 斯洛文尼亚语 */\n" +
                "SL(\"sl\"),\n" +
                "/** 奥利亚语 */\n" +
                "OR(\"or\"),\n" +
                "/** 马拉亚拉姆语 */\n" +
                "ML(\"ml\"),\n" +
                "/** 亚美尼亚语 */\n" +
                "HY(\"hy\"),\n" +
                "/** 德语 */\n" +
                "DE(\"de\"),\n" +
                "/** 蒙古语 */\n" +
                "MN(\"mn\"),\n" +
                "/** 波斯语 */\n" +
                "FA(\"fa\"),\n" +
                "/** 马达加斯加语 */\n" +
                "MG(\"mg\"),\n" +
                "/** 刚果语 */\n" +
                "KG(\"kg\"),\n" +
                "/** 马其顿语 */\n" +
                "MK(\"mk\"),\n" +
                "/** 依地语 */\n" +
                "YI(\"yi\"),\n" +
                "/** 格鲁吉亚语 */\n" +
                "KA(\"ka\"),\n" +
                "/** 卡纳达语 */\n" +
                "KN(\"kn\"),\n" +
                "/** 丹麦语 */\n" +
                "DA(\"da\"),\n" +
                "/** 科西嘉语 */\n" +
                "CO(\"co\"),\n" +
                "/** 巴斯克语 */\n" +
                "EU(\"eu\"),\n" +
                "/** 阿拉伯语 */\n" +
                "AR(\"ar\"),\n" +
                "/** 马耳他语 */\n" +
                "MT(\"mt\"),\n" +
                "/** 南索托语 */\n" +
                "ST(\"st\"),\n" +
                "/** 斯瓦希里语 */\n" +
                "SW(\"sw\"),\n" +
                "/** 加泰隆语 */\n" +
                "CA(\"ca\"),\n" +
                "/** 阿塞拜疆语 */\n" +
                "AZ(\"az\"),\n" +
                "/** 高棉语 */\n" +
                "KM(\"km\"),\n" +
                "/** 拉脱维亚语 */\n" +
                "LV(\"lv\"),\n" +
                "/** 现代希腊语 */\n" +
                "EL(\"el\"),\n" +
                "/** 塞尔维亚语 */\n" +
                "SR(\"sr\"),\n" +
                "/** 阿非利堪斯语 */\n" +
                "AF(\"af\"),\n" +
                "/** 波斯尼亚语 */\n" +
                "BS(\"bs\"),\n" +
                "/** 冰岛语 */\n" +
                "IS(\"is\"),\n" +
                "/** 维吾尔语 */\n" +
                "UG(\"ug\"),\n" +
                "/** 毛利语 */\n" +
                "MI(\"mi\"),\n" +
                "/** 基尼阿万达语 */\n" +
                "RW(\"rw\"),\n" +
                "/** 印地语 */\n" +
                "HI(\"hi\"),\n" +
                "/** 书面挪威语 */\n" +
                "NB(\"nb\"),\n" +
                "/** 伊博语 */\n" +
                "IG(\"ig\"),\n" +
                "/** 尼扬贾语 */\n" +
                "NY(\"ny\"),\n" +
                "/** 尼泊尔语 */\n" +
                "NE(\"ne\"),\n" +
                "/** 泰米尔语 */\n" +
                "TA(\"ta\"),\n" +
                "/** 波兰语 */\n" +
                "PL(\"pl\"),\n" +
                "/** 信德语 */\n" +
                "SD(\"sd\"),\n" +
                "/** 匈牙利语 */\n" +
                "HU(\"hu\"),\n" +
                "/** 普什图语 */\n" +
                "PS(\"ps\"),\n" +
                "/** 阿尔巴尼亚语 */\n" +
                "SQ(\"sq\"),\n" +
                "/** 吉尔吉斯语 */\n" +
                "KY(\"ky\"),\n" +
                "/** 世界语 */\n" +
                "EO(\"eo\"),\n" +
                "/** 白俄罗斯语 */\n" +
                "BE(\"be\"),\n" +
                "/** 爱尔兰语 */\n" +
                "GA(\"ga\"),\n" +
                "/** 科萨语 */\n" +
                "XH(\"xh\"),\n" +
                "/** 缅甸语 */\n" +
                "MY(\"my\"),\n" +
                "/** 加利西亚语 */\n" +
                "GL(\"gl\"),\n" +
                "/** 塔加洛语 */\n" +
                "TL(\"tl\"),\n" +
                "/** 朝鲜语、韩语 */\n" +
                "KO(\"ko\"),\n" +
                "/** 塔吉克语 */\n" +
                "TG(\"tg\"),\n" +
                "/** 威尔士语 */\n" +
                "CY(\"cy\"),\n" +
                "/** 索马里语 */\n" +
                "SO(\"so\"),\n" +
                "/** 葡萄牙语 */\n" +
                "PT(\"pt\"),\n" +
                "/** 越南语 */\n" +
                "VI(\"vi\"),\n" +
                "/** 旁遮普语 */\n" +
                "PA(\"pa\"),\n" +
                "/** 俄语 */\n" +
                "RU(\"ru\"),\n" +
                "/** 日语 */\n" +
                "JA(\"ja\"),\n" +
                "/** 塔塔尔语 */\n" +
                "TT(\"tt\"),\n" +
                "/** 意大利语 */\n" +
                "IT(\"it\"),\n" +
                "/** 爱沙尼亚语 */\n" +
                "ET(\"et\"),\n" +
                "/** 土耳其语 */\n" +
                "TR(\"tr\"),\n" +
                "/** 豪萨语 */\n" +
                "HA(\"ha\"),\n" +
                "/** 罗马尼亚语 */\n" +
                "RO(\"ro\"),\n" +
                "/** 法语 */\n" +
                "FR(\"fr\"),\n" +
                "/** 古吉拉特语 */\n" +
                "GU(\"gu\"),\n" +
                "/** 祖鲁语 */\n" +
                "ZU(\"zu\"),\n" +
                "/** 约鲁巴语 */\n" +
                "YO(\"yo\"),\n" +
                "/** 西班牙语 */\n" +
                "ES(\"es\"),\n" +
                "/** 摩尔达维亚语 */\n" +
                "MO(\"mo\"),\n" +
                "/** 僧加罗语 */\n" +
                "SI(\"si\"),\n" +
                "/** 荷兰语 */\n" +
                "NL(\"nl\"),\n" +
                "/** 马来语 */\n" +
                "MS(\"ms\"),\n" +
                "/** 卢森堡语 */\n" +
                "LB(\"lb\"),\n" +
                "/** 捷克语 */\n" +
                "CS(\"cs\"),\n" +
                "/** 中文、汉语 */\n" +
                "ZH(\"zh\"),\n" +
                "/** 挪威语 */\n" +
                "NO(\"no\"),\n" +
                "/** 绍纳语 */\n" +
                "SN(\"sn\"),\n" +
                "/** 弗里西亚语 */\n" +
                "FY(\"fy\"),\n" +
                "/** 土库曼语 */\n" +
                "TK(\"tk\"),\n" +
                "/** 库尔德语 */\n" +
                "KU(\"ku\"),\n" +
                "/** 乌尔都语 */\n" +
                "UR(\"ur\"),\n" +
                "/** 老挝语 */\n" +
                "LO(\"lo\"),\n" +
                "/** 芬兰语 */\n" +
                "FI(\"fi\"),\n" +
                "/** 印尼语 */\n" +
                "ID(\"id\"),\n" +
                "/** 哈萨克语 */\n" +
                "KK(\"kk\"),\n" +
                "/** 苏格兰盖尔语 */\n" +
                "GD(\"gd\"),\n" +
                "/** 拉丁语 */\n" +
                "LA(\"la\"),\n" +
                "/** 瑞典语 */\n" +
                "SV(\"sv\"),\n" +
                "/** 泰卢固语 */\n" +
                "TE(\"te\"),\n" +
                "/** 孟加拉语 */\n" +
                "BN(\"bn\"),\n" +
                "/** 乌克兰语 */\n" +
                "UK(\"uk\"),\n" +
                "/** 立陶宛语 */\n" +
                "LT(\"lt\"),\n" +
                "/** 泰语 */\n" +
                "TH(\"th\"),\n" +
                "/** 斯洛伐克语 */\n" +
                "SK(\"sk\"),\n" +
                "/** 海地克里奥尔语 */\n" +
                "HT(\"ht\"),\n" +
                "/** 巽他语 */\n" +
                "SU(\"su\"),";

        List<String> enumList = StrUtil.split(s, ',', true, true);
        enumList.sort(Comparator.comparing(TranslaterDemo::comp));
        for (String s1 : enumList) {
            System.out.println(a(s1));
        }




    }
    private static String comp(String s){
        List<String> split = StrUtil.split(s, '(', true, true);
        return split.get(1);
    }

    private static String a(String s){
        String desc = s.substring(s.indexOf("/** ")+4, s.indexOf(" */"));
        String replace = s.replace(")", ",\"" + desc + "\",false),");
        return replace.substring(replace.indexOf("*/")+2,replace.lastIndexOf(",")+1);
    }
}
