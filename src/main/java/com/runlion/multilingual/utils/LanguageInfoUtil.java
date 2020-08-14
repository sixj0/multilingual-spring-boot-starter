/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.runlion.multilingual.utils;

import com.runlion.multilingual.enums.LanguageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author sixiaojie
 * @date 2019-12-13 18:11
 */
@Slf4j
public class LanguageInfoUtil {

    private static final ThreadLocal<String> LANGUAGE_INFO_THREAD_LOCAL = new InheritableThreadLocal<>();

    private static String getLanguageInfo() {
        return LANGUAGE_INFO_THREAD_LOCAL.get();
    }

    public static void removeLanguageInfo() {
        LANGUAGE_INFO_THREAD_LOCAL.remove();
    }

    private static void setLanguageInfo(String languageInfo) {
        LANGUAGE_INFO_THREAD_LOCAL.set(languageInfo);
    }



    /**
     * 获取当前语言类型
     * @return
     */
    public static String getCurrentLanguage() {
        String languageInfo = getLanguageInfo();
        if (languageInfo == null) {
            languageInfo = initUserInfo();
            if(languageInfo == null){
                // 默认语言为中文
                languageInfo = LanguageEnum.ZH_CN.getDesc();
            }
        }
        setLanguageInfo(languageInfo);
        return languageInfo;
    }

    private static String initUserInfo() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        // 从请求头中获取语言类型
        String lang = httpServletRequest.getHeader("Language");
        if (lang != null) {
            // 统一处理为小写
            String lowerCase = lang.toLowerCase();
            setLanguageInfo(lowerCase);
            return lowerCase;
        }
        return null;
    }

}
